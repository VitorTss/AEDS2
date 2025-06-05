import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class Ex14 {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner entrada = new Scanner(System.in);

        try {
            RandomAccessFile arquivo = new RandomAccessFile("dados.txt", "rw");

            int quantidade = entrada.nextInt();

            for (int i = 0; i < quantidade; i++) {
                double valor = entrada.nextDouble();
                arquivo.writeDouble(valor);
            }

            arquivo.close();

            arquivo = new RandomAccessFile("dados.txt", "r");

            int tamanhoDouble = 8;

            for (int i = quantidade - 1; i >= 0; i--) {
                arquivo.seek(i * tamanhoDouble);
                double valor = arquivo.readDouble();

                if (valor == (long) valor) {
                    System.out.println((long) valor);
                } else {
                    System.out.println(valor);
                }
            }

            arquivo.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        entrada.close();
    }
}
