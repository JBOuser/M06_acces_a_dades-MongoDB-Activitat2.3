package dam.m06.mongodb.Activitat_2_3;

import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

public class ComandaDB {
	
	//create a new comanda requesting data
	public static Document enterTheDataForANewComanda()
	{
		String date = Comanda.enterDate();
		
		/*
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(date, dateFormatter);
		*/
		
		int import_comanda = Client.enterInteger("Enter the MIN import. It must be an integer (p.e. 99): ");

		String pagada = "";
		do
		{
			System.out.printf("Enter 'true' or 'false'. It sets if factura is paid or not: ");
			pagada = ClientDB.sc.nextLine();
		}
		while(!pagada.toLowerCase().matches("true|false"));
		
		Document newComanda = new Document("date",date)
				.append("import_comanda", import_comanda)
				.append("pagada", pagada);
		
		return newComanda;
	}	

	public static Document insertComandaToClient(Document client, Document newComanda)
	{
		List<Document> comandes = client.getList("comandes", Document.class);

		//check if the field "comandes" exist
		if(comandes == null)
		{
			//create a new Comanda list and add newComanda
			comandes = new ArrayList<Document>();
		}
		//add newComanda to other existing Comandes
		comandes.add(newComanda);
		
		//update total_facturacio's content
		String field = "total_facturacio";
		if(client.containsKey(field))
		{
			int newImport = client.getInteger("total_facturacio") + newComanda.getInteger("import_comanda");
			//remove("total_facturacio");
			client.replace(field, newImport);
		}
		else
		{
			client.append(field, newComanda.getInteger("import_comanda"));
		}
		
		//comandes list<Document> is added as Array filled with several Document inside
		client.append("comandes",comandes);
		
		return client;
	}	
}
