
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

		do {
			System.out.print("> ");
			Scanner s = new Scanner(in.readLine());
			int estado = 0;
			String token;
			String nombre = null;
			String opcion = null;
			while (estado != 8) {
				switch (estado) {
				case 0:
					try {
						token = s.skip("fin|buscar|borrar|guardar|cargar|\\p{L}+(\\s+\\p{L}+)*").match().group();
						if (token.equals("fin")) {
							estado = 8;
							fin = true;
						} else if (token.equals("buscar")) {
							opcion = token;
							estado = 2;
						} else if (token.equals("borrar")) {
							opcion = token;
							estado = 2;
						} else if (token.equals("guardar")) {
							opcion = token;
							estado = 2;
						} else if (token.equals("cargar")) {
							opcion = token;
							estado = 2;
						} else {
							nombre = token;
							estado = 1;
						}
					} catch (NoSuchElementException e) {
						System.out.println("Se esperaba 'buscar' o 'fin' o un nombre.");
						estado = 8;
					}
					break;
				case 1:
					try {
						s.skip("-");
						estado = 3;
					} catch (NoSuchElementException e) {
						System.out.println("Se esperaba '-'.");
						estado = 8;
					}
					break;
				case 2:
					try {
						s.skip(":");
						if (opcion.equals("buscar"))
							estado = 4;
						else if (opcion.equals("borrar"))
							estado = 5;
						else if (opcion.equals("guardar"))
							estado = 6;
						else if (opcion.equals("cargar"))
							estado = 7;
					} catch (NoSuchElementException e) {
						System.out.println("Se esperaba ':'.");
						estado = 8;
					}
					break;
				// nombre-telefono
					
				case 3:
					try {
						token = s.skip("\\d{9}").match().group();
						if (agenda.containsKey(nombre))
							System.out.println("El contacto " + nombre + " ha sido actualizado: " + agenda.get(nombre)
									+ " -> " + token);
						agenda.put(nombre, token);
						estado = 8;
					} catch (NoSuchElementException e) {
						System.out.println("Se esperaba un teléfono.");
						estado = 8;
					}
					break;
				// buscar:nombre
					
				case 4:
					try {
						token = s.skip("\\p{L}+(\\s+\\p{L}+)*").match().group();
						String telefono = agenda.get(token);
						if (telefono != null)
							System.out.println(token + " -> " + telefono);
						else
							System.out.println(token + " no se encuentra en la agenda.");
						estado = 6;
					} catch (NoSuchElementException e) {
						System.out.println("Se esperaba un nombre.");
						estado = 8;
					}
					break;
				// borrar:nombre
					
				case 5:
					try {
						token = s.skip("\\p{L}+(\\s+\\p{L}+)*").match().group();
						String telefono = agenda.get(token);
						if (telefono != null) {
							System.out.println("Contacto eliminado : " + token + " -> " + telefono);
							agenda.remove(token);
						} else
							System.out.println(token + " no se encuentra en la agenda.");
						estado = 8;
					} catch (NoSuchElementException e) {
						System.out.println("Se esperaba un nombre.");
						estado = 8;
					}
					break;
					
				// guardar:ruta
				case 6:
					break;
					
				// cargar:ruta
				case 7:
					token = s.skip("\\p{L}+(\\s+\\p{L}+)*").match().group();
					String ruta = "C:\\agenda.txt";
					File fichero = new File(ruta);
					Scanner s2 = null;
					try {
						s = new Scanner(fichero);
						while (s.hasNextLine()) {
							String linea = s.nextLine();
							System.out.println(linea);
						}
					} catch (Exception e) {
						System.out.println("Mensaje: " + e.getMessage());
						estado = 8;
					} finally {
						try {
							if (s != null) {
								s.close();
								estado = 8;
							}
						} catch (Exception ex2) {
							System.out.println("Mensaje 2: " + ex2.getMessage());
							estado = 8;
						}
					}
					break;
				}

			}
			s.close();
		} while (!fin);
		System.out.println(agenda);
	}
}
