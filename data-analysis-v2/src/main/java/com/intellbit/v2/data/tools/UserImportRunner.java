package com.intellbit.v2.data.tools;

import org.apache.log4j.PropertyConfigurator;

import com.intellbit.v2.data.importer.UserImporter;

public class UserImportRunner {

	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("Error Parameters.\n  "
					+ "UserImportRunner <config direcory> <excel file>.");
			System.exit(1);
		}
		String conf = args[0];
		String excel = args[1];
		PropertyConfigurator.configure(conf + "\\log4j.properties");
		UserImporter importer = UserImporter.getImporter(conf);
		importer.doImport(excel);
	}
	
}
