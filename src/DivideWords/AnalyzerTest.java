package DivideWords;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.io.*;

class Analyzer {
	String words = "";
	int words_len = 0;
	int max_length = 7;// 分词最大长度
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

	public String RMMSplit() {// 逆向匹配
		int words_len = getWordsLength();
		if (words_len == 0)
			return "";
		String si = "";
		int pattern_len = words_len >= max_length ? max_length : words_len;// 最大匹配长度
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
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}// 读入词典
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
					new FileInputStream(file));// 建立一个输入流对象
			BufferedReader br = new BufferedReader(reader);// 建立一个对象,把文件内容转换成计算机能都读懂的语言

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
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}

			out.write("\r\n");
			out.flush();
			out.close();
			br.close();

			System.out.println("分词文本 : " + parentname + "\\" + filename);
			System.out.println("分词数目 : " + count);
			System.out.println();
			Allcount += count;
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}//
	}

	public void isDictionary(File f, ArrayList<String> pathlist) {
		// 判断传入对象是否为一个文件夹对象
		File[] t = f.listFiles();
		for (int i = 0; i < t.length; i++) {
			if (t[i].isDirectory())
				isDictionary(t[i], pathlist);
			else {
				try {
					pathlist.add(t[i].getCanonicalPath());
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		String pathname = "F:\\数字内容安全\\实验四\\dataset_616979\\616979\\体育领域\\体育分类训练文档\\";
		String savepath = "F:\\数字内容安全\\实验四\\分词结果\\体育分类训练文档\\";
		String dictpath = "F:\\数字内容安全\\实验四\\词典.txt";
		AnalyzerTest at = new AnalyzerTest();
		ArrayList<String> pathlist = new ArrayList<String>();
		File f = new File(pathname);
		at.isDictionary(f, pathlist);
		String path = null;
		File save=new File(savepath);
		if(!save.exists())
			save.mkdirs();
		long startTime = System.currentTimeMillis();// 分词开始时间
		for (int i = 0; i < pathlist.size(); i++) {
			path = pathlist.get(i);
			at.DivideWords(path, savepath, dictpath);
		}
		long endTime = System.currentTimeMillis();// 分词结束时间
		long Time = endTime - startTime;// 运行时间
		System.out.println("程序运行时间 : " + Time + "ms");
		System.out.println("总共分词" + Allcount + "个");
	}
}