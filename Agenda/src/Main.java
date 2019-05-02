
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {
		Map<String, String> agenda = new HashMap<>();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		boolean fin = false;
		File file = new File("C:\\agenda.txt"); 
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));

		do {
			System.out.print("> ");
			Scanner s = new Scanner(in.readLine());
			int estado = 0;
			String token;
			String nombre = null;
			String opcion = null;
			while (estado != 6) {
				switch (estado) {
				case 0:
					try {
						token = s.skip("fin|buscar|borrar|guardar|\\p{L}+(\\s+\\p{L}+)*").match().group();
						if (token.equals("fin")) {
							estado = 6;
							fin = true;
						}
						else if (token.equals("buscar")) {
							opcion = token;
							estado = 2;
						}
						else if (token.equals("borrar")) {
							opcion = token;
							estado = 2;
						}
						else if (token.equals("guardar")) {
							opcion = token;
							estado = 2;
						}
						else {
							nombre = token;
							estado = 1;
						}
					} catch (NoSuchElementException e) {
						System.out.println("Se esperaba 'buscar' o 'fin' o un nombre");
						estado = 6;
					}
					break;
				case 1:
					try {
						s.skip("-");
						estado = 3;
					}catch (NoSuchElementException e) {
						System.out.println("Se esperaba '-'");
						estado = 6;
					}
					break;
				case 2:
					try {
						s.skip(":");
						if (opcion.equals("buscar"))
							estado = 4;
						else if (opcion.equals("borrar"))
							estado = 5;
					}catch (NoSuchElementException e) {
						System.out.println("Se esperaba ':'");
						estado = 6;
					}
					break;
				case 3:
					try {
						token = s.skip("\\d{9}").match().group();
						if (agenda.containsKey(nombre)) 
							System.out.println("El contacto " + nombre + " ha sido actualizado: " + agenda.get(nombre) + " -> " + token);
						agenda.put(nombre, token);
						estado = 6;
					}catch (NoSuchElementException e) {
						System.out.println("Se esperaba un teléfono");
						estado = 6;
					}
					break;
				case 4:
					try {
						token = s.skip("\\p{L}+(\\s+\\p{L}+)*").match().group();
						String telefono = agenda.get(token);
						if (telefono != null)
							System.out.println(token + " -> " + telefono);
						else
							System.out.println(token + " no se encuentra en la agenda");
						estado = 6;
					} catch (NoSuchElementException e) {
						System.out.println("Se esperaba un nombre");
						estado = 6;
					}
					break;
				case 5:
					try {
						token = s.skip("\\p{L}+(\\s+\\p{L}+)*").match().group();
						String telefono = agenda.get(token);
						if (telefono != null) {
							System.out.println("Contacto eliminado : " + token + " -> " + telefono);
							agenda.remove(token);
						}
						else
							System.out.println(token + " no se encuentra en la agenda");
						estado = 6;
					} catch (NoSuchElementException e) {
						System.out.println("Se esperaba un nombre");
						estado = 6;
					}
					break;
				}
			}
			s.close();
		} while (!fin);
		System.out.println(agenda);
		for(String nombre:agenda.keySet())
		   {
		      bw.write(nombre + " -> " + agenda.get(nombre));
		      bw.newLine();
		   }
		   bw.flush();
		   bw.close();

	}

}