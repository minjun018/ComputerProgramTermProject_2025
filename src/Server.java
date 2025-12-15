import admin.Account;
import admin.UserManage;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int RESERVATION_PORT = 1224;
    private static final int LOGIN_PORT = 9999; // 8080은 충돌나서 9999로 변경

    private static final String FILE_PATH = "src/admin/rs.txt";

    public static void main(String[] args) {
        System.out.println(">>> 서버 통합 시작...");

        new Thread(() -> {
            ExecutorService pool = Executors.newFixedThreadPool(10);
            try (ServerSocket serverSocket = new ServerSocket(RESERVATION_PORT)) {
                System.out.println(">>> 예약 관리 서버 대기 중 (Port: " + RESERVATION_PORT + ")");
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    pool.execute(new ClientHandler(clientSocket));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(LOGIN_PORT)) {
                System.out.println(">>> 로그인 서버 대기 중 (Port: " + LOGIN_PORT + ")");
                while (true) {
                    Socket socket = serverSocket.accept();
                    System.out.println(">>> 클라이언트 접속됨: " + socket.getInetAddress());
                    new Thread(() -> handleLogin(socket)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    static class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            ) {
                String request = in.readLine();

                if (request != null) {
                    if (request.equals("LOAD")) {
                        handleLoad(out);
                    } else if (request.startsWith("SAVE")) {
                        handleSave(request.substring(5));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void handleLoad(PrintWriter out) {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                out.println("EOF");
                return;
            }
            synchronized (Server.class) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            out.println("EOF");
        }

        private void handleSave(String data) {
            synchronized (Server.class) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
                    if (new File(FILE_PATH).length() > 0) {
                        bw.newLine();
                    }
                    bw.write(data);
                    System.out.println("데이터 저장됨: " + data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void handleLogin(Socket socket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String request = in.readLine();
            System.out.println(">>> 요청 받음: " + request);

            if (request != null) {
                if (request.startsWith("LOGIN")) {
                    String[] parts = request.split("\\|");
                    if (parts.length == 3) {
                        String id = parts[1];
                        String pw = parts[2];

                        Account account = UserManage.Login(id, pw);

                        if (account != null) {
                            out.println("SUCCESS|" + account.id + "|" + account.pw + "|" + account.manage + "|" + account.uni_number);
                            System.out.println(">>> 로그인 성공 응답 보냄");
                        } else {
                            out.println("FAIL");
                            System.out.println(">>> 로그인 실패 응답 보냄");
                        }
                    }
                } else if (request.startsWith("REGISTER")) {
                    String[] parts = request.split("\\|");
                    if (parts.length == 5) {
                        String id = parts[1];
                        String pw = parts[2];
                        boolean manage = Boolean.parseBoolean(parts[3]);
                        int major = Integer.parseInt(parts[4]);

                        UserManage.register(id, pw, manage, major);
                        out.println("SUCCESS");
                        System.out.println(">>> 회원가입 성공 응답 보냄");
                    } else {
                        out.println("FAIL");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(">>> 에러 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}