package sto.krachmi;

import org.dom4j.Element;

public class Krachma {
public String name="Alehouse";
public String text;
public String grad="София";
public String address="където";
public String phonenumber="42141";
public String website="вввв.фафс.ком";
public boolean activated=false;

public Krachma(String tname,String ttext,String tgrad,String taddress,String tphonenumber,String twebsite){
	name=tname;
	text=ttext;
	grad=tgrad;
	address=taddress;
	phonenumber=tphonenumber;
	website=twebsite;
}

public Krachma(Element krachma){
	name=krachma.getName();
	address=krachma.elementByID("address").getText();
}
}
