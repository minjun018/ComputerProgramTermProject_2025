

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 1224;
    private static final String FILE_PATH = "src/admin/rs.txt";

    public static void main(String[] args) {
        System.out.println(">>> 예약 관리 서버 시작...");
        ExecutorService pool = Executors.newFixedThreadPool(10);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                pool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                        handleSave(request.substring(5)); // "SAVE " 뒤의 데이터
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
                    System.out.println("저장 완료: " + data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}