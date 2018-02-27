package DivideWords;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.io.*;

class Analyzer {
	String words = "";
	int words_len = 0;
	int max_length = 7;// �ִ���󳤶�
	Set<String> dict = new HashSet<String>();
	ArrayList<String> results = new ArrayList<String>();

	public Analyzer(String words, Set<String> dict) {
		this.words = words;
		words_len = this.words.length();
		this.dict = dict;
	}

	public int getWordsLength() {
		return words.length();
	}

	public String RMMSplit() {// ����ƥ��
		int words_len = getWordsLength();
		if (words_len == 0)
			return "";
		String si = "";
		int pattern_len = words_len >= max_length ? max_length : words_len;// ���ƥ�䳤��
		for (int i = pattern_len; i >= 1; i--) {
			si = words.substring(words_len - i);
			if (Find(si) || i == 1) {
				words = words.substring(0, words_len - i);
				results.add(si);
				break;
			}
		}
		return si;
	}

	public boolean Find(String str) {
		String word = null;
		if (dict.contains(str))
			return true;
		else
			return false;

	}

	public static void DictInit(Set<String> dict, BufferedReader dictbr) {
		if (dict.isEmpty()) {
			String word = "";
			try {
				word = dictbr.readLine();
				while (word != null) {
					dict.add(word);
					word = dictbr.readLine();
				}
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}// ����ʵ�
	}
}

public class AnalyzerTest {
	static int Allcount = 0;

	public void DivideWords(String pathname, String savepath, String dictpath) {
		File f = new File(pathname);
		try {
			String filename = f.getName();
			String parentname = f.getParentFile().getName();
			File file = new File(pathname);
			InputStreamReader reader = new InputStreamReader(
					new FileInputStream(file));// ����һ������������
			BufferedReader br = new BufferedReader(reader);// ����һ������,���ļ�����ת���ɼ�����ܶ�����������

			File dictionary = new File(dictpath);
			InputStreamReader dictreader = new InputStreamReader(
					new FileInputStream(dictionary));
			BufferedReader dictbr = new BufferedReader(dictreader);

			Set<String> dict = new HashSet<String>();
			Analyzer.DictInit(dict, dictbr);
			dictbr.close();

			File writepath = new File(savepath + parentname + "\\");
			if (!writepath.exists())
				writepath.mkdir();
			File writename = new File(savepath + parentname + "\\" + filename);
			writename.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));

			int count = 0;
			String temp = null;
			String str = "";
			try {
				str = br.readLine();
				while (str != null) {
					Analyzer a = new Analyzer(str, dict);
					String isFinish = a.RMMSplit();
					while (isFinish != "")
						isFinish = a.RMMSplit();
					for (int j = a.results.size(); j > 0; j--) {
						temp = a.results.remove(j - 1);
						out.write(temp + " ");
						if (temp.length() != 1)
							count++;
					}
					out.write("\r\n");
					str = br.readLine();
				}
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}

			out.write("\r\n");
			out.flush();
			out.close();
			br.close();

			System.out.println("�ִ��ı� : " + parentname + "\\" + filename);
			System.out.println("�ִ���Ŀ : " + count);
			System.out.println();
			Allcount += count;
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}//
	}

	public void isDictionary(File f, ArrayList<String> pathlist) {
		// �жϴ�������Ƿ�Ϊһ���ļ��ж���
		File[] t = f.listFiles();
		for (int i = 0; i < t.length; i++) {
			if (t[i].isDirectory())
				isDictionary(t[i], pathlist);
			else {
				try {
					pathlist.add(t[i].getCanonicalPath());
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		String pathname = "F:\\�������ݰ�ȫ\\ʵ����\\dataset_616979\\616979\\��������\\��������ѵ���ĵ�\\";
		String savepath = "F:\\�������ݰ�ȫ\\ʵ����\\�ִʽ��\\��������ѵ���ĵ�\\";
		String dictpath = "F:\\�������ݰ�ȫ\\ʵ����\\�ʵ�.txt";
		AnalyzerTest at = new AnalyzerTest();
		ArrayList<String> pathlist = new ArrayList<String>();
		File f = new File(pathname);
		at.isDictionary(f, pathlist);
		String path = null;
		File save=new File(savepath);
		if(!save.exists())
			save.mkdirs();
		long startTime = System.currentTimeMillis();// �ִʿ�ʼʱ��
		for (int i = 0; i < pathlist.size(); i++) {
			path = pathlist.get(i);
			at.DivideWords(path, savepath, dictpath);
		}
		long endTime = System.currentTimeMillis();// �ִʽ���ʱ��
		long Time = endTime - startTime;// ����ʱ��
		System.out.println("��������ʱ�� : " + Time + "ms");
		System.out.println("�ܹ��ִ�" + Allcount + "��");
	}
}