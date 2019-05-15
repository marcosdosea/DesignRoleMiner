package com.github.drminer.smelldetector.negocio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LeitorEscritorArquivo
{

	private Object dados;
	private String arquivo;

	public LeitorEscritorArquivo(Object dados, String nome_arq)
	{
		this.dados = dados;
		this.arquivo = nome_arq;
	}

	public Object readFromFile() throws IOException, ClassNotFoundException
	{
		ObjectInputStream is = new ObjectInputStream(new FileInputStream(arquivo));
		Object savedBook = is.readObject();
		is.close();
		return savedBook;
	}

	public void salvarContatos() throws IOException
	{
		File destino = new File(arquivo );
		System.out.println(destino.getAbsolutePath());
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(destino));
		os.writeObject(dados);
		os.close();
	}

}
