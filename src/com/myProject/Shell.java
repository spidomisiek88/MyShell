package com.myProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class Shell
{
	private static final String DEFAULT_PROMPT = "$";
	private static final String COMMAND_PROMPT = "prompt";
	private static final String COMMAND_DIR = "dir";
	private static final String COMMAND_TREE = "tree";
	private static final String COMMAND_CD = "cd";
	private static final String COMMAND_CD_PARAM_DOTDOT = "..";
	private static final String COMMAND_EXIT = "exit";
	private static final String COMMAND_PROMPT_PARAM_RESET = "reset";
	private static final String COMMAND_PROMPT_PARAM_$CWD = "$cwd";

	private String prompt = DEFAULT_PROMPT;
	private Scanner in = new Scanner(System.in);
	private File currentDirectory = new File(".");
	private File[] filesList = currentDirectory.listFiles();

	public void printMainPrompt()
	{
		System.out.print(getMainPrompt());
	}
	private String getMainPrompt()
	{
		return "[" + MyShell.class.getSimpleName() + "] ";
	}

	public void printPrompt()
	{
		System.out.print(getTagPrompt());
	}
	
	private String getTagPrompt()
	{
		return prompt + ">";
	}

	public String printDirInfo()
	{
		StringBuilder contentCurrentDirectory = new StringBuilder();
		
		contentCurrentDirectory.append("Content of " + printCurrentWorkingDirectory(currentDirectory));
		
		for (File file : filesList)
		{
			if (file.isDirectory())
				contentCurrentDirectory.append("\nDIR     " + file.getName());
			else if (file.isFile())
				contentCurrentDirectory.append("\nFILE    " + file.getName());
		}

		return contentCurrentDirectory.toString();
	}

	public String printDirTree(String treeStruct)
	{
		int indent = 0;
		StringBuilder stringBuilder = new StringBuilder();
		
		createDirTree(currentDirectory, indent, stringBuilder);

		return stringBuilder.toString();
	}
	
	private void createDirTree(File directory, int indent, StringBuilder stringBuilder)
	{
		if (currentDirectory.isDirectory())
		{
			stringBuilder.append(getIndentString(indent));
			if (directory.getName().equals("."))
				stringBuilder.append(Paths.get("").toAbsolutePath().getFileName().toString());
			else
				stringBuilder.append(directory.getName());
			stringBuilder.append("\n");
			for (File dir : directory.listFiles())
			{
				if (dir.isDirectory())
					createDirTree(dir, indent + 1, stringBuilder);
			}
		}
	}
	
	private String getIndentString(int indent)
	{
		StringBuilder indentStringBuilder = new StringBuilder();
		for (int i = 0; i < indent; i++)
		{
			indentStringBuilder.append("-");
		}
		
		return indentStringBuilder.toString();
	}

	public String getPrompt()
	{
		return prompt;
	}

	public void setPrompt(String prompt)
	{
		this.prompt = prompt;
	}
	
	private String printCurrentWorkingDirectory(File directory)
	{
		String cwdPath = "";
		try
		{
			cwdPath = directory.getCanonicalPath();
		}
		catch (IOException ioException)
		{
			ioException.printStackTrace();
		}
		
		return cwdPath;
	}
	
	private boolean isInDirectory(File searchedDirectory, String wantedDirectory)
	{
		boolean isFind = false;
		
		for (File directory : searchedDirectory.listFiles())
		{
			if (directory.isDirectory())
				if (directory.getName().equals(wantedDirectory))
					isFind = true;
		}
		
		return isFind;
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
					setPrompt(printCurrentWorkingDirectory(currentDirectory));
					isListen = true;
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
			else if (command.equals(COMMAND_TREE))
			{
				System.out.println(printDirTree(""));
				isListen = true;
			}
			else if (command.equals(COMMAND_CD))
			{
				String param = in.next();
				if (param.equals(COMMAND_CD_PARAM_DOTDOT))
				{
					if (null == currentDirectory.getParentFile())
						currentDirectory = new File(".");
					else
						currentDirectory = new File(printCurrentWorkingDirectory(currentDirectory.getParentFile().getParentFile().getParentFile()), printCurrentWorkingDirectory(currentDirectory.getParentFile().getParentFile())+ "\\.");
//						currentDirectory = new File(currentDirectory.getParentFile().getParentFile(), printCurrentWorkingDirectory(currentDirectory.getParentFile().getParentFile())+ "\\.");
					setPrompt(printCurrentWorkingDirectory(currentDirectory));
					isListen = true;
				}
				else if (!param.isEmpty())
				{
					if (isInDirectory(currentDirectory, param))
					{
						currentDirectory = new File(currentDirectory, param + "\\.");
						setPrompt(printCurrentWorkingDirectory(currentDirectory));
					}
					else
						System.out.println(printDirTree(""));
					isListen = true;
				}
			}
			else if (command.equals(COMMAND_EXIT))
				isListen = false;
			else
			{
				System.out.println(command + " : unknown command");
				isListen = true;
			}
		}
		return isListen;
	}
}
