package dam.m06.mongodb.Activitat_2_3;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;


/**
 * Hello world!
 *
 */
public class App 
{
	private static Scanner sc = new Scanner(System.in);

	private static String[] menu_options = {"\nMENU",
			"1.Donar d'alta un client",
			"2.Afegir una nova comanda a un client",
			"3.Cercar clients per facturació",
			"4.Cercar clients per quantitat de comandes",
			"5.DNI de tots els clients",
			"0.Sortir"};
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//avoid verbose lines in console
		Logger.getLogger("org.mongodb").setLevel(Level.OFF);		

		String option = "";
		do {
			//DB's name
			String db_name = "clients_comandes";
			
			for(String line : menu_options) {System.out.println(line);}
			System.out.println("\nEnter an option: ");
			option = sc.next();
			
			if(option.matches("1|2|3|4|5"))
			{
				switch(Integer.parseInt(option))
				{
					case 1:
						/* 
						Exercici 1
						
						Crea i prova un mètode que afegeixi un client a la BBDD. De moment s'afegirà sempre 
						el mateix client, sense demanar res per teclat. Les dades d'aquest client poden ser:
						  
						nif: 12345678X
						nom: Pere Pons
						total_facturacio: 0
						
						Els camps correu, telefon i comandes no s'informen. Comprova des de la consola de 
						mongoDB que s'ha inserit correctament.
						 */
						
						/*
						Client newClient1 = new Client("12345678X","Pere Pons",0);
						ClientDB.addClientToDB(db_name, ClientDB.convertClientToDocument(newClient1));
						
						Client newClient2 = new Client("23456789B","Sarah Sans",0);
						ClientDB.addClientToDB(db_name, ClientDB.convertClientToDocument(newClient2));

						Client newClient3 = new Client("34567891C","Joan Jorba",0);
						ClientDB.addClientToDB(db_name, ClientDB.convertClientToDocument(newClient3));						
						*/
						
						/*
						Exercici 2
						
						Partint del mètode anterior, fes una millora de forma que es demanin per teclat 
						les dades del client. A més, demana per teclat el telefon i el client. Si l'usuari 
						no els introdueix, el programa no els inserirà. Important! No els ha d'inserir, és 
						a dir, no és correcte inserir-los i deixar-los en blanc.						 
						 */	
						
						ClientDB.enterTheDataForANewClient(db_name);
						break;
					
					case 2:
						
						/*
						Exercici 4

						Amplia l'exericici anterior, de forma que l'usuari pugui seleccionar un dels NIF. 
						Sobre el NIF seleccionat s'afegirà una nova comanda, per a la qual es demanara a 
						l'usuari la data de la comanda i la quantitat total facturada.

						Exemple:
						1) 12345678X
						2) 98765432Y

						L'usuari selecciona el client 2, corresponent al NIF 98765432Y. El programa demana
						per teclat la data i l'import facturada, i insereix la comanda al client 
						sol·licitat, incrementant el camp total_facturacio amb l'import de la factura.
						Nota: Tot i que existeixen mètodes d'actualització de documents, nosaltres, per 
						simplificar, esborrarem el document i l'afegirem de nou amb les dades modificades.
						*/

						//choose an existing client in DB
						Document chosenClient = ClientDB.choseAClientFromDB(db_name);
						if(chosenClient != null) { 
							
							//enter data of new Comanda
							Document newComanda = ComandaDB.enterTheDataForANewComanda();
							
							//add new Comanda to client
							chosenClient = ComandaDB.insertComandaToClient(chosenClient, newComanda);
							
							//add the updated client again to DB 
							ClientDB.addClientToDB(db_name, chosenClient);
						}
						break;
						
					case 3:
						
						/*
						Exercici 5
						
						Crea i prova un mètode que permeti buscar clients en funció de la seva facturació. 
						Es permetrà buscar els clients que tinguin una facturació superior a la introduïda. 
						Per exemple, si l'usuari introdueix 1000, es recuperaran tots els clients amb 
						facturació superior a 1000. Per als clients trobats, es mostraran tots els seus atributs.
						*/
						
						//https://kb.objectrocket.com/mongo-db/how-to-use-mongodb-comparison-query-operators-in-java-382
						
						int facturacioQuantity = Client.enterInteger("Enter the import. It must be an integer (p.e. 99): ");
						ClientDB.printDocumentList(
							 ClientDB.getClientsWithEqualsOrGreaterFacturacioThanSpecified(db_name, String.valueOf(facturacioQuantity)));
						break;							
						
					case 4:
						
						/*
						Exercici 6
						
						Crea i prova un mètode que permeti buscar clients en funció de la seva quantitat de comandes.
						Es permetrà buscar els clients que tinguin una quantitat de comandes superior a la introduïda. Per
						exemple, si l'usuari introdueix 10, es recuperaran tots els clients amb 5 o més factures. Per als 
						clients trobats, es mostraran tots els seus atributs.
						*/
						
						int comandesQuantity = Client.enterInteger("Enter the minimum length of client's comandes. It must be an integer (p.e. 2): ");
						ClientDB.printDocumentList(
								ClientDB.getAllClientsFilteredByComandesLength(db_name, comandesQuantity));
						break;						
						
					case 5:
						
						/*
						Exercici 3
						Crea i prova un mètode que llisti tots els NIF dels clients que hi ha a la BBDD.
						*/
						String fieldName = "dni";
						System.out.printf("Clients '%s' values: \n",fieldName);
						ClientDB.printStringList(
								ClientDB.getEachValueOfAnSpecifiedField(
										ClientDB.getAllClientsAsListFromDB(db_name),fieldName));
						break;							
						
					default:
						if(!option.matches("0")) {System.out.printf("Wrong option: %s\n",option);}
				}
			}
			else
			{
				if(!option.matches("0")) {System.out.printf("Wrong option: %s\n",option);}
			}
			
		}while(!option.matches("0"));
		
		System.out.println("Closing ...");
	}
}

