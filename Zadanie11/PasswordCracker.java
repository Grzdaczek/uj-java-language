import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordCracker implements PasswordCrackerInterface {

	String encodePassword(List<Integer> password, List<Character> schema) {
		assert(password.size() == schema.size());
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < schema.size(); ++i) {
			var component = PasswordComponents.passwordComponents.get(schema.get(i));
			var ch = component.get(password.get(i));
			builder.append(ch);
		}
		
		return builder.toString();
	}

	@Override
	public String getPassword(String host, int port) {
		try {
			Socket socket = new Socket(host, port);
			OutputStream os = socket.getOutputStream();
			InputStream is = socket.getInputStream();

			PrintWriter writer = new PrintWriter(os);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			
			reader.readLine(); //gibberish

			writer.println("Program");
			writer.flush();
		
			reader.readLine(); //more gibberish
			String line = reader.readLine().replaceFirst("schema : ", "");
			reader.readLine(); //even more gibberish

			List<Character> schema = PasswordComponents.decodePasswordSchema(line);
			List<Integer> password = new ArrayList<Integer>(Collections.nCopies(schema.size(), 0));
			
			Integer i = 0;
			Integer lastSolved = null;

			while (true) {

				writer.println(encodePassword(password, schema));
				writer.flush();
				line = reader.readLine();

				if (line.equals("+OK")) {
					socket.close();
					return encodePassword(password, schema);
				}
				else if (line.contains("To nie hasło")) {
					Integer solved = Integer.decode(line.replace("To nie hasło, odgadnięto ", "").replace(" znaków", ""));

					// how many solved on first try
					if (lastSolved == null)
						lastSolved = solved;

					// System.err.println( encodePassword(password, schema) + "\t solved " + solved + "\tlastSolved " + lastSolved);
					if (solved == lastSolved + 1) 		i += 1;
					else if (solved == lastSolved)		password.set(i, password.get(i) + 1);
					else if (solved == lastSolved - 1)	password.set(i, password.get(i) - 1);

					lastSolved  = solved;
				}
				else {
					socket.close();
					throw new Exception("Unexpected server response: " + line);
				}
			}
		}
		catch (Exception ex) {}

		return null;
	}
}