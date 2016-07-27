package com.myProject;

import java.nio.file.Paths;

public class MyShell
{

	public static void main(String[] args)
	{
		Shell shell = new Shell();
		boolean isAction = true;
		
		while (isAction)
		{
			shell.printMainPrompt();
			shell.printPrompt();
			isAction = shell.listenShell();
		}
		
		System.out.println("Bye\n");
		System.out.println(Paths.get("").toAbsolutePath().toString() + ">");
	}
}
