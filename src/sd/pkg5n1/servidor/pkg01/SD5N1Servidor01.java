
package sd.pkg5n1.servidor.pkg01;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 *
 * @author Bruno
 */
public class SD5N1Servidor01 {

    ServerSocket serverSocket;

    public static void main(String[] args) {
        /* 
         1. Criar o servidor de conexões
         2. Esperar o pedido de conexão 
           2.1 Criar nova conexão
         3. Criar streams de entrada e saída
         4. Tratar a conversação entre cliente e servidor
         5. Voltar para passo 2 
        */
        SD5N1Servidor01 servidor = new SD5N1Servidor01();
        try {
            System.out.println("1) Criando Servidor");
            servidor.criaServidor(5555);
            while ( true ) {
                System.out.println("2) Esperando Conexão");
                Socket socket = servidor.esperaConexao(); // 2 e 2.1
                System.out.println("3) Tratando Conexão");
                servidor.trataConexao(socket); // 3 e 4
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        
    }
    
    public void criaServidor (int porta) throws IOException {
        serverSocket = new ServerSocket(porta);
    }
    
    public Socket esperaConexao () throws IOException {
        Socket socket = serverSocket.accept();
        return socket;
    }
    
    public void trataConexao ( Socket socket ) throws IOException {
        
        // criar streams de entrada e saída
        DataInputStream input = new DataInputStream( socket.getInputStream() );
        DataOutputStream output = new DataOutputStream( socket.getOutputStream() );
        
        System.out.println("Tratando conexão...");
        
        // recebe informação
        String msgCliente = "";
        while (!msgCliente.equals("EXIT")) {
            msgCliente = input.readUTF();
            System.out.println("Mensagem recebida: " + msgCliente);
            // envia informação
            // padrão que utilizaremos operacao#parametro1#parametro2#parametroN / operacao#listagem:elem1,elem2,elem3
            String resposta = null;
            String[] protocolo = msgCliente.split("#");
            if (protocolo.length > 2) {
                if (protocolo[0].equals("SOMA")) {
                    resposta = adicao(protocolo);
                }
                else if (protocolo[0].equals("SUBTRACAO")) {
                    resposta = subtracao(protocolo);
                }
                else if (protocolo[0].equals("MULTIPLICACAO")) {
                    resposta = multiplicacao(protocolo);
                }
                else if (protocolo[0].equals("DIVISAO")) {
                    resposta = divisao(protocolo);
                }
                else {
                    resposta = "erro#SEMOPERACAO";
                }
            }
            else {
                if (protocolo[0].equals("EXIT")) {
                    System.out.println("Cliente desconectando...");
                    socket.close();
                    msgCliente = "EXIT";
                }
                else {
                    resposta = "erro#FPARAM";
                }
            }
            output.writeUTF(resposta);
        }
        
    }

    public static String adicao (String[] protocolo) {
        try {
            Integer valor1 = Integer.parseInt(protocolo[1]);
            Integer valor2 = Integer.parseInt(protocolo[2]);
            Integer valorTotal = valor1 + valor2;
            return "somareply#" + valorTotal;
        } catch (Exception e) {
            return "ERRO#FPARAM: " + e.getMessage();
        }
    }
    
    private static String subtracao(String[] protocolo) {
        try {
            Integer valor1 = Integer.parseInt(protocolo[1]);
            Integer valor2 = Integer.parseInt(protocolo[2]);
            Integer valorTotal = valor1 - valor2;
            return "subtracaoreply#" + valorTotal;
        } catch (Exception e) {
            return "ERRO#FPARAM: " + e.getMessage();
        }
    }
    
    private static String multiplicacao(String[] protocolo) {
        try {
            Integer valor1 = Integer.parseInt(protocolo[1]);
            Integer valor2 = Integer.parseInt(protocolo[2]);
            Integer valorTotal = valor1 * valor2;
            return "multreply#" + valorTotal;
        } catch (Exception e) {
            return "ERRO#FPARAM: " + e.getMessage();
        }
    }

    private String divisao(String[] protocolo) {
        try {
            Integer valor1 = Integer.parseInt(protocolo[1]);
            Integer valor2 = Integer.parseInt(protocolo[2]);
            Integer valorTotal = valor1 / valor2;
            return "multreply#" + valorTotal;
        } catch (Exception e) {
            return "erro#DIVISAOPORZERO";
        }
    }
    
}
