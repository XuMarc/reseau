package trame;

import analyser.Tools;

import java.math.BigInteger;

public class IPv4 {
	private String version="";
	private String ihl="";
	private String tos="";
	private String totalLength="";
	private String identification="";
	private String flagsAndFragmentOffset=""; //attribut temporaire
	private String[] flags= {"0","0","0"};
	private String fragmentOffset="";
	private String ttl="";
	private String protocol="";
	private String headerChecksum="";
	private String sourceAdress="";
	private String destinationAdress="";
	private String data="";
	private TCP tcp;
	
	public IPv4(String trame) {
		version+=""+trame.charAt(0);
		
		ihl+=""+trame.charAt(1);
		
		tos+=""+trame.charAt(2)+trame.charAt(3);
		
		totalLength+=""+trame.charAt(4)+trame.charAt(5);
		totalLength+=""+trame.charAt(6)+trame.charAt(7);
		
		identification+=""+trame.charAt(8)+trame.charAt(9);
		identification+=""+trame.charAt(10)+trame.charAt(11);
		
		flagsAndFragmentOffset+=""+trame.charAt(12)+trame.charAt(13);
		flagsAndFragmentOffset+=""+trame.charAt(14)+trame.charAt(15);
		String bin = Tools.hexToBin(flagsAndFragmentOffset);
		
		flags[0] =""+bin.charAt(0);
		flags[1] =""+bin.charAt(1);
		flags[2] =""+bin.charAt(2);
		
		for (int i = 3; i < bin.length(); i++) fragmentOffset += ""+bin.charAt(i);
		
		ttl+=""+trame.charAt(16)+trame.charAt(17);

		protocol+=""+trame.charAt(18)+trame.charAt(19);
		
		headerChecksum+=""+trame.charAt(20)+trame.charAt(21);
		headerChecksum+=""+trame.charAt(22)+trame.charAt(23);
		
		sourceAdress+=""+trame.charAt(24)+trame.charAt(25);
		sourceAdress+=""+trame.charAt(26)+trame.charAt(27);
		sourceAdress+=""+trame.charAt(28)+trame.charAt(29);
		sourceAdress+=""+trame.charAt(30)+trame.charAt(31);
		
		destinationAdress+=""+trame.charAt(32)+trame.charAt(33);
		destinationAdress+=""+trame.charAt(34)+trame.charAt(35);
		destinationAdress+=""+trame.charAt(36)+trame.charAt(37);
		destinationAdress+=""+trame.charAt(38)+trame.charAt(39);
		int dataLength = getDataLength();
		int debutData = 40 + optionLength()*8;
		System.out.print(dataLength);
		for (int i = debutData; i < dataLength - 1; i+=1){
			data +="" + trame.charAt(i);
		}
		this.doTCP();
	}
	
	public String toString() {
		String res ="";
		res +="IPv4\n";
		res += "	   Version : "+Integer.parseInt(version, 16)+"\n";
		res += "	   IP Header Length : "+Integer.parseInt(ihl,16)+" bytes"+" 0x"+ihl+"\n";
		res += "	   Type of Service : "+"0x"+tos+"\n";
		res += "	   Total Length : "+Integer.parseInt(totalLength, 16)+" bytes"+"\n";
		res += "	   Identification : "+"0x"+identification+"\n";
		res += "	   Flags :\n";
		res += "	     Reserved bit : "+flags[0]+"\n";
		res += "	     DF : "+flags[1]+"\n";
		res += "	     MF : "+flags[2]+"\n";
		res += "	   Fragment Offset : "+"0b"+fragmentOffset+"\n";
		res += "	   Time To Live : "+Integer.parseInt(ttl, 16)+"\n";
		res += "	   Protocol : "+IPv4.whatProtocol(protocol)+"\n";
		res += "	   Header Checksum unverified : "+headerChecksum+"\n";
		res += "	   Protocol : "+IPv4.whatProtocol(protocol)+"\n";
		res += "	   Source Adress : "+Tools.hexToAddressIP(sourceAdress)+"\n";
		res += "	   Destination Adress : "+Tools.hexToAddressIP(destinationAdress)+"\n";
		return res;
	}
	

	
	public static String whatProtocol(String s) {
		int val = Integer.parseInt(s, 16);
		if (val == 1) return "ICMP(1)";
		if (val == 2) return "IGMP(2)";
		if (val == 6) return "TCP(6)";
		if (val == 8) return "EGP(8)";
		if (val == 9) return "IGP(9)";
		if (val == 17) return "UDP(17)";
		if (val == 36) return "XTP(36)";
		if (val == 46) return "RSVP(46)";
		return "";
	}

	public boolean hasTCP() {
		return IPv4.whatProtocol(protocol).equals("TCP(6)");
	}
	
	public void doTCP() {
		if (hasTCP()) tcp =  new TCP(data);
	}
	
	public TCP getTCP() {
		return this.tcp;
	}
	
	public int getDataLength() {
		int nbttl = Integer.parseInt(ttl, 16);
		System.out.println(nbttl);
		int nbihl = Integer.parseInt(ihl, 16);
		System.out.println(nbihl);

		return nbttl - nbihl;
	}
	
	public int optionLength() {
		return 5-Integer.parseInt(ihl,16);

	}
	
	
	
	
}