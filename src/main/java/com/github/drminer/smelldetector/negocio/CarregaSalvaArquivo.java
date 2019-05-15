package com.github.drminer.smelldetector.negocio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.github.drminer.smelldetector.model.LimiarMetrica;
import com.github.drminer.smelldetector.model.LimiarTecnica;

public class CarregaSalvaArquivo {

	private static LeitorEscritorArquivo LEAProjetos;

	@SuppressWarnings("unchecked")
	public static ArrayList<String> carregarProjetos() {
		ArrayList<String> projetos = new ArrayList<>();
		LEAProjetos = new LeitorEscritorArquivo(projetos, "Projetos.txt");

		try {
			projetos = (ArrayList<String>) LEAProjetos.readFromFile();
			System.out.println("Dados dos alunos carregados com sucesso!");
		} catch (Exception e1) {
			System.out.println("Erro: Arquivo dos alunos não encontrado! \nEstamos criando um novo arquivo.");
		} finally {
			LEAProjetos = null;
		}
		return projetos;
	}

	@SuppressWarnings("unchecked")
	public static List<LimiarTecnica> carregarLimiares(String pastaDestino) {
		ArrayList<LimiarTecnica> limiarTecnicas = new ArrayList<>();
		String diretorioLimiares = pastaDestino;
		File file = new File(diretorioLimiares);
		File afile[] = file.listFiles();
		int i = 0;
		for (int j = afile.length; i < j; i++) {
			File arquivoLimiar = afile[i];
			try {
				HashMap<String, LimiarMetrica> limiares = new HashMap<>();
				LimiarTecnica limiarTecnica = new LimiarTecnica();
				limiarTecnica.setTecnica(arquivoLimiar.getName().substring(0, arquivoLimiar.getName().indexOf(".csv")));
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

			} catch (IOException e1) {
				System.out.println("Erro ao na leitura de arquivos de limiares");
			}
		}

		return limiarTecnicas;
	}

	// joga arrayList em arquivo txt

	public static void salvaArquivo(ArrayList<String> projetos) {
		LEAProjetos = new LeitorEscritorArquivo(projetos, "Projetos.txt");
		try {
			LEAProjetos.salvarContatos();
			System.out.println("Dados dos alunos salvos com sucesso!");
		} catch (IOException e1) {
			System.out.println("Erro ao salvar dados dos alunos!");
			e1.printStackTrace();
		}
	}

}
