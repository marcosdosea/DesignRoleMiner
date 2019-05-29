package org.designroleminer.smelldetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.designroleminer.smelldetector.model.LimiarMetrica;
import org.designroleminer.smelldetector.model.LimiarTecnica;

public class CarregaSalvaArquivo {

	@SuppressWarnings("unchecked")
	public static ArrayList<String> carregarProjetos() {
		ArrayList<String> projetos = new ArrayList<>();
		try {
			projetos = (ArrayList<String>) readFromFile(projetos, "Projetos.txt");
			System.out.println("Dados dos alunos carregados com sucesso!");
		} catch (Exception e1) {
			System.out.println("Erro: Arquivo dos alunos não encontrado! \nEstamos criando um novo arquivo.");
		}
		return projetos;
	}

	@SuppressWarnings("unchecked")
	public static List<LimiarTecnica> carregarLimiares(String pastaDestino) {
		ArrayList<LimiarTecnica> limiarTecnicas = new ArrayList<>();
		// String diretorioLimiares = pastaDestino;
		File file = new File(pastaDestino);
		File afile[] = file.listFiles();
		int i = 0;
		for (int j = afile.length; i < j; i++) {
			File arquivoLimiar = afile[i];
			try {
				HashMap<String, LimiarMetrica> limiares = new HashMap<>();
				LimiarTecnica limiarTecnica = new LimiarTecnica();
				if (arquivoLimiar.getName().contains(".csv")) {
					limiarTecnica
							.setTecnica(arquivoLimiar.getName().substring(0, arquivoLimiar.getName().indexOf(".csv")));
					limiarTecnica.setMetricas(limiares);
					Scanner scanner = new Scanner(arquivoLimiar);
					scanner.useDelimiter(";");

					if (scanner.hasNextLine()) {
						String cabecalho = scanner.nextLine();
						while (scanner.hasNext() && cabecalho.contains(LimiarMetrica.METRICA_LOC)) {
							String designRole = scanner.next();

							LimiarMetrica loc = new LimiarMetrica();
							loc.setDesignRole(designRole);
							loc.setLimiarMaximo(scanner.nextInt());
							loc.setMetrica(LimiarMetrica.METRICA_LOC);
							limiares.put(loc.getKey(), loc);

							LimiarMetrica cc = new LimiarMetrica();
							cc.setDesignRole(designRole);
							cc.setLimiarMaximo(scanner.nextInt());
							cc.setMetrica(LimiarMetrica.METRICA_CC);
							limiares.put(cc.getKey(), cc);

							LimiarMetrica efferent = new LimiarMetrica();
							efferent.setDesignRole(designRole);
							efferent.setLimiarMaximo(scanner.nextInt());
							efferent.setMetrica(LimiarMetrica.METRICA_EC);
							limiares.put(efferent.getKey(), efferent);

							LimiarMetrica nop = new LimiarMetrica();
							nop.setDesignRole(designRole);
							nop.setLimiarMaximo(scanner.nextInt());
							nop.setMetrica(LimiarMetrica.METRICA_NOP);
							limiares.put(nop.getKey(), nop);
							if (scanner.hasNextLine())
								scanner.nextLine();
						}
						limiarTecnicas.add(limiarTecnica);
					}

					scanner.close();
				}

			} catch (IOException e1) {
				System.out.println("Erro ao na leitura de arquivos de limiares");
			}
		}

		return limiarTecnicas;
	}

	// joga arrayList em arquivo txt

	public static void salvaArquivo(ArrayList<String> projetos) {
		try {
			saveFile(projetos, "Projetos.txt");
			System.out.println("Dados dos alunos salvos com sucesso!");
		} catch (IOException e1) {
			System.out.println("Erro ao salvar dados dos alunos!");
			e1.printStackTrace();
		}
	}

	public static Object readFromFile(Object dados, String file) throws IOException, ClassNotFoundException {
		ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
		Object savedBook = is.readObject();
		is.close();
		return savedBook;
	}

	public static void saveFile(Object dados, String file) throws IOException {
		File destino = new File(file);
		System.out.println(destino.getAbsolutePath());
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(destino));
		os.writeObject(dados);
		os.close();
	}

}
