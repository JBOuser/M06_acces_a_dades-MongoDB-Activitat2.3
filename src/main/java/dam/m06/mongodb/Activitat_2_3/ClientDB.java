package dam.m06.mongodb.Activitat_2_3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class ClientDB {

	public static Scanner sc = new Scanner(System.in); 
	
	//Exercici 1
	public static void addClientToDB(String db_name, Document document)
	{
		try(MongoClient mongoClient = MongoClients.create();){
			
			MongoDatabase mongoDatabase = mongoClient.getDatabase(db_name);
			MongoCollection<Document> clients = mongoDatabase.getCollection("client");
			
			//insert Document in DB
			clients.insertOne(document);	
			
			System.out.println("Client '"+document.getString("dni")+"' saved");
		}
		catch(Exception e)
		{
			System.out.println("Client '"+document.getString("dni")+"' not saved");
		}		
		
	}
	
	//Exercici 2
	public static void enterTheDataForANewClient(String db_name)
	{
		String dni = Client.enterDni();
		
		System.out.println("Introdueix el nom: ");
		String nom = sc.nextLine();
		
		int total_facturacio = Client.enterInteger(
				"Enter facturacio (p.e. 99): ");
		
		String telefon = Client.enterTelefon();
		
		String email = Client.enterEmail();
		
		Client newClient = new Client(dni, nom, total_facturacio);
		newClient.setTelefon(telefon);
		newClient.setEmail(email);
		
		//if returns null newClient has no empty fields. Otherwise return which field is empty
		if(Client.checkEmptyFields(newClient) != null)
		{
			addClientToDB(db_name, ClientDB.convertClientToDocument(newClient));
		}
	}
	
	//Exercici 3
	//return a List of all Assignatura from DB
	public static ArrayList<Document> getAllClientsAsListFromDB(String db_name)
	{
		ArrayList<Document> clientsFromDB = null;
		
		try(MongoClient mongoClient = MongoClients.create();){
			
			MongoDatabase mongoDatabase = mongoClient.getDatabase(db_name);
			MongoCollection<Document> clientCollection = mongoDatabase.getCollection("client");
	
			//return each client inside a List from DB
			List<Document> allClients = (List<Document>) clientCollection.find().into(
					new ArrayList<Document>()); 
			
			if(allClients != null)
			{
				clientsFromDB = (ArrayList<Document>)allClients;
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}

		return clientsFromDB;
	}
	
	
	//Exercici 3
	public static ArrayList<String> getEachValueOfAnSpecifiedField(ArrayList<Document> collectionArrayList, String fieldName)
	{
		ArrayList<String> valuesFromField = null;
		
		if(collectionArrayList != null && 0 < collectionArrayList.size())
		{
			valuesFromField = new ArrayList<String>();
			//iterate over entered collection
			for(Document collectionName : collectionArrayList)
			{
				//get the 'dni' content of each client and add it into 'dniClientsFromDB'
				valuesFromField.add(collectionName.getString(fieldName));
			}				
		}
		
		return valuesFromField;
	}
	
	
	//Exercici 4
	//return a client as a Document if there is a match
	public static Document getClientByDniFromDB(String db_name, String dniToSearch)
	{
		Document clientMatched = null;
		
		ArrayList<Document> allClientsFromDB = getAllClientsAsListFromDB(db_name);
		if(allClientsFromDB != null)
		{
			int i = 0;
			boolean matchedFound = false;
			while(i < allClientsFromDB.size() && !matchedFound)
			{
				if(allClientsFromDB.get(i).getString("dni").matches(dniToSearch))
				{
					clientMatched = allClientsFromDB.get(i);
					matchedFound = true;
				}
				else i++;
			}
		}

		return clientMatched;
	}	
	
	
	
	//Exercici 4
	//find a client and remove it from DB if its dni matches 
	public static Document findAndRemoveAClientIfMatchesTheEnteredField(String db_name, String fieldName, String fieldValue)
	{
		Document documentMatched = null;
		ArrayList<Document> allClientsFromDB = null;
		
		try(MongoClient mongoClient = MongoClients.create();){
			
			MongoDatabase mongoDatabase = mongoClient.getDatabase(db_name);
			MongoCollection<Document> clientCollection = mongoDatabase.getCollection("client");
	
			//find a Document that matches the Filters condition, return it and then remove it from DB 
			documentMatched = clientCollection.findOneAndDelete(Filters.eq(fieldName, fieldValue));
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}	
		return documentMatched;
	}	
	
	
	//Exercici4
	public static Document choseAClientFromDB(String db_name)
	{
		Document matchedClient = null;
		
		try(MongoClient mongoClient = MongoClients.create();)
		{
			MongoDatabase mongoDatabase = mongoClient.getDatabase(db_name);
			MongoCollection<Document> clientCollection = mongoDatabase.getCollection("client");
	
			//return each client inside an ArrayList from DB
			ArrayList<Document> allClientsFromDB  = (ArrayList<Document>) clientCollection.find().into(
					new ArrayList<Document>()); 			
			
			if(allClientsFromDB!=null && 0 < allClientsFromDB.size())
			{
				//map each client's dni to a number 
				System.out.println("Clients from DB: ");
				int i = 1;
				Map<String,String> availableClients = new HashMap<String, String>();
				
				for( String dni : getEachValueOfAnSpecifiedField(
						allClientsFromDB, 
						"dni"))
				{
					availableClients.put(String.valueOf(i), dni);
					System.out.println(i+"."+dni);
					i++;
				}
				System.out.println("Choose a client by its numbers (p.e. 1): ");
				String option4 = ClientDB.sc.nextLine();
				
				if(availableClients.containsKey(option4))
				{
					//get and remove the client that matches
					matchedClient = ClientDB.findAndRemoveAClientIfMatchesTheEnteredField(
							db_name,"dni", availableClients.get(option4));	
				}
				else
				{
					System.out.printf("Client with number '%s' not found\n",option4);
				}
			}
			else
			{
				System.out.println("No 'client' found in DB");
			}				
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		return matchedClient;
	}

	
	//Exercici 5
	public static ArrayList<Document> getClientsWithEqualsOrGreaterFacturacioThanSpecified(String db_name,String value)
	{
		ArrayList<Document> clients = null;
		
		try(MongoClient mongoClient = MongoClients.create();)
		{
			MongoDatabase mongoDatabase = mongoClient.getDatabase(db_name);
			MongoCollection<Document> clientCollection = mongoDatabase.getCollection("client");
	
			if(value.matches("[0-9]+")){
				//return each client inside an ArrayList from DB
				FindIterable<Document> allClientsFromDB  = clientCollection.find(Filters.gte("total_facturacio", Integer.parseInt(value)));

				if(allClientsFromDB != null)
				{
					clients = new ArrayList<Document>();
					for(Document client : allClientsFromDB)
					{
						clients.add(client);
					}
				}				
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		return clients;
	}
	
	
	//Exercici 6
	public static ArrayList<Document> getAllComandesFromClientById(String db_name, String dniToSearch)
	{
		ArrayList<Document> comandes = null;
		FindIterable<Document> cursor = null;

		try(MongoClient mongoClient = MongoClients.create();){
			
			MongoDatabase mongoDatabase = mongoClient.getDatabase(db_name);
			MongoCollection<Document> clients = mongoDatabase.getCollection("client");
			
			cursor = clients.find(Filters.eq("dni", dniToSearch));
			Iterator<Document> iterator = cursor.iterator();
			
			if(iterator.hasNext())
			{
				//get the Assignatura that matched
				Document documentTmp = iterator.next();
				
				//get all Client from Assignatura
				comandes = (ArrayList<Document>) documentTmp.getList("comandes", Document.class);
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}		
		
		return comandes;
	}	
	
	
	//Exerci 6
	public static ArrayList<Document> getAllClientsFilteredByComandesLength(String db_name, int comandesMinLength)
	{
		ArrayList<Document> filteredClients = null;
		List<Document> allClients = getAllClientsAsListFromDB(db_name);		
		if(allClients != null)
		{
			filteredClients = new ArrayList<Document>();
			for(Document client : allClients)
			{
				//get all Client from Assignatura
				List<Document> allComandesClient = (ArrayList<Document>) client.getList("comandes", Document.class);
				if(allComandesClient != null && comandesMinLength <= allComandesClient.size())
				{
					filteredClients.add(client);
				}
			}
		}
		return filteredClients;
	}		
	
	
	//convert a Client's instance to org.bson.Document's instance
	public static Document convertClientToDocument(Client client)
	{
		//create a new Document which works as instance in MongoDB
		Document document = new Document("dni",client.getDni())
				.append("nom", client.getNom())
				.append("total_facturacio", client.getTotal_facturacio());
		
		if(client.getEmail()!="") {document.append("email", client.getEmail());}
		if(client.getTelefon()!="") {document.append("telefon", client.getTelefon());}
		return document;
	}
	
	
	//Enter an ArrayList<String> as param and print each String 
	public static void printDocumentList(ArrayList<Document> documentsList)
	{
		if(documentsList != null && 0 < documentsList.size())
		{
			documentsList.forEach(doc -> { System.out.println(doc.toString()); });			
		}
		else System.out.println("No 'Document' to print");
	}	
	
	
	//Enter an ArrayList<String> as param and print each String 
	public static void printStringList(ArrayList<String> stringList)
	{
		if(stringList != null && 0 < stringList.size())
		{
			stringList.forEach(string -> { System.out.println(string); });			
		}
		else System.out.println("No 'content' to print");
	}
	
}
