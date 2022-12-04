package analyser;
import trame.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Analyser {

	public static List<String> execute(String filePath) throws IOException {
		//On récupère la liste des trames
		List<String> trameFinale = Tools.getCleanFrame(filePath);
		
		String res;
		List<String> list = new ArrayList<>();

		StringBuilder textBuilder = new StringBuilder();
		//Pour chaque trame
		for (String trame : trameFinale) {
			//On crée une entete Ethernet
			Ethernet e = new Ethernet(trame);
			//On ajoute le decodage de l'ethernet dans res
			res=""+e.toString();
			textBuilder.append(e.toString());
			//On crée l'entete Ipv4
			IPv4 ip = e.getIpv4();
			//Si il n'y a pas d'IPv4
			if (ip == null) {
				res +="Pas de IPv4\n";
				textBuilder.append("Pas de IPv4\n");
				list.add(res);
				continue;
			}
			//Sinon on ajoute le décodage dans res
			textBuilder.append(ip.toString());
			res+=""+ip.toString();
			//On crée l'entete TCP
			TCP tcp = ip.getTCP();
			//Si il n'y a pas de TCP
			if (tcp == null) {
				textBuilder.append("Pas de TCP\n");
				list.add(res);
				continue;
			}
			//Sinon on rajoute le décodage dans res
			textBuilder.append(tcp.toString());
			res+=""+tcp.toString();
			//On crée le HTTP
			HTTP http = tcp.getHTTP();
			//Si il n'y a pas de HTTP
			if (http == null) {
				textBuilder.append("Pas de HTTP\n");
				list.add(res);
				continue;
			}
			//Sinon on rajoute le décodage dans res
			res+=""+http.toString(false);
			textBuilder.append(http.toString());
			list.add(res);
		}
		String text = textBuilder.toString();
		Tools.ecrire(text);
		return list;

	}
}
