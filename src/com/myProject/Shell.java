package com.myProject;

import java.io.File;
import java.util.Scanner;

public class Shell
{
	private static final String DEFAULT_PROMPT = "$";
	private static final String COMMAND_PROMPT = "prompt";
	private static final String COMMAND_DIR = "dir";
	private static final String COMMAND_EXIT = "exit";
	private static final String COMMAND_PROMPT_PARAM_RESET = "reset";
	private static final String COMMAND_PROMPT_PARAM_$CWD = "$cwd";
	
	private String prompt = DEFAULT_PROMPT;
	private Scanner in = new Scanner(System.in);
	
	public void printMainPrompt()
	{
		System.out.print("[" + MyShell.class.getSimpleName() + "] ");
	}
	
	public void printPrompt()
	{
		System.out.print(prompt + ">");
	}
	
	public String printDirInfo()
	{
		String contentCurrentDirectory = "\nContent of " + this.getClass().getClassLoader().getResource("").getPath();
		File currentDirectory = new File(".");
		File[] filesList = currentDirectory.listFiles();
		for (File file : filesList)
		{
			if (file.isDirectory())
				contentCurrentDirectory = contentCurrentDirectory + "\nDIR     " + file.getName();
			else if (file.isFile())
				contentCurrentDirectory = contentCurrentDirectory + "\nFILE    " + file.getName();
		}
		
		return contentCurrentDirectory;
	}
	
	public String printDirTree()
	{
		String treeStructureCurrentDirectory = "";
		
		return treeStructureCurrentDirectory;
	}

	public String getPrompt()
	{
		return prompt;
	}

	public void setPrompt(String prompt)
	{
		this.prompt = prompt;
	}
	
	public boolean listenShell()
	{
		boolean isListen = false;
		if (in.hasNext())
		{
			String command = in.next();
			if (command.equals(COMMAND_PROMPT))
			{
				String param = in.next();
				if (param.equals(COMMAND_PROMPT_PARAM_RESET))
				{
					setPrompt(DEFAULT_PROMPT);
					isListen = true;
				}
				else if (param.equals(COMMAND_PROMPT_PARAM_$CWD)) 
				{
					printMainPrompt();
					System.out.println(this.getClass().getClassLoader().getResource("").getPath());
				}
				else 
				{
					setPrompt(in.next());
					isListen = true;
				}
			}
			else if (command.equals(COMMAND_DIR)) 
			{
				System.out.println(printDirInfo());
				isListen = true;
			}
			else if (command.equals(COMMAND_EXIT)) 
				isListen = false;
			else
				isListen = true;
		}
		return isListen;
	}
}
