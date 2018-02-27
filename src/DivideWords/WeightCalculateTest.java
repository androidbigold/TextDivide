package DivideWords;

import java.io.*;
import java.text.DateFormat;
import java.util.*;
import java.util.regex.*;

class Idf {
	private String word;
	private double idf;

	public Idf(String word) {
		this.word = word;
		idf = 0.0;
	}

	public Idf(String word, double idf) {
		this.word = word;
		this.idf = idf;
	}

	public void setIdf(double idf) {
		this.idf = idf;
	}

	public String getWord() {
		return this.word;
	}

	public double getIdf() {
		return this.idf;
	}

	public boolean equals(Object otherObject) {
		if (this == otherObject)
			return true;
		if (otherObject == null)
			return false;
		if (getClass() != otherObject.getClass())
			return false;
		Idf other = (Idf) otherObject;
		return word.equals(other.word);
	}
}

class Tf {
	private String word;
	private double tf;

	public Tf(String word) {
		this.word = word;
		tf = 0.0;
	}

	public Tf(String word, double tf) {
		this.word = word;
		this.tf = tf;
	}

	public void setTf(double tf) {
		this.tf = tf;
	}

	public String getWord() {
		return this.word;
	}

	public double getTf() {
		return this.tf;
	}

	public boolean equals(Object otherObject) {
		if (this == otherObject)
			return true;
		if (otherObject == null)
			return false;
		if (getClass() != otherObject.getClass())
			return false;
		Tf other = (Tf) otherObject;
		return word.equals(other.word);
	}
}

class Weight {
	private String word;
	private double weight;

	public Weight(String word) {
		this.word = word;
		weight = 0.0;
	}

	public Weight(String word, double weight) {
		this.word = word;
		this.weight = weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getWord() {
		return this.word;
	}

	public double getWeight() {
		return this.weight;
	}
}

class WeightCompute {
	static Pattern patPunc = Pattern
			.compile("[`~!@#$^&*()=|{}':;'\",\\[\\].<>/?~！@#￥……&*〉（）——|{}【】‘；：”“'。，、？12345667890《》-]");

	public static double IdfCalculate(String word, ArrayList<String> pathlist) {
		double idf = 0.0;
		int count = pathlist.size();
		int wordcount = 0;

		for (int i = 0; i < pathlist.size(); i++) {
			try {
				File file = new File(pathlist.get(i));
				InputStreamReader reader = new InputStreamReader(
						new FileInputStream(file));
				BufferedReader br = new BufferedReader(reader);// 建立一个对象,把文件内容转换成计算机能都读懂的语言

				String line = br.readLine();
				while (line != null) {
					if (line.indexOf(word) != -1) {
						wordcount++;
						break;
					}
					line = br.readLine();
				}
				br.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}// 建立一个输入流对象
		}
		if (wordcount != 0)
			idf = Math.log((double) (count / wordcount));
		else
			idf = 0.0;
		return idf;
	}

	public static void IdfListCalculate(ArrayList<String> pathlist,
			ArrayList<Idf> IdfList) {
		String savepath = "F:\\数字内容安全\\实验四\\权重计算\\体育分类训练文档\\idf.txt";
		File result = new File(savepath);
		if (result.exists()) {
			try {
				InputStreamReader reader = new InputStreamReader(
						new FileInputStream(result));
				BufferedReader br = new BufferedReader(reader);// 建立一个对象,把文件内容转换成计算机能都读懂的语言
				String str = "";
				int temp = 0;
				String word = null;
				double idf = 0.0;
				str = br.readLine();
				while (str != null) {
					temp = str.indexOf(" ");
					word = str.substring(0, temp);
					idf = Double.parseDouble(str.substring(temp + 1,
							str.length()));
					IdfList.add(new Idf(word, idf));
					str = br.readLine();
				}
				br.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}// 建立一个输入流对象
		} else {
			try {
				result.createNewFile();
				BufferedWriter out = new BufferedWriter(new FileWriter(result));

				String[] strlist = new String[] {};
				for (int i = 0; i < pathlist.size(); i++) {
					File file = new File(pathlist.get(i));
					InputStreamReader reader = new InputStreamReader(
							new FileInputStream(file));// 建立一个输入流对象
					BufferedReader br = new BufferedReader(reader);// 建立一个对象,把文件内容转换成计算机能都读懂的语言

					String str = "";
					str = br.readLine();
					while (str != null) {
						Matcher m = patPunc.matcher(str);
						str = m.replaceAll("").trim();
						strlist = str.split(" ");
						for (int j = 0; j < strlist.length; j++) {
							if (IdfList.contains(new Idf(strlist[j]))
									|| strlist[j].equals(""))
								continue;
							else
								IdfList.add(new Idf(strlist[j]));
						}
						str = br.readLine();
					}
					br.close();
				}

				String word = null;
				double idf = 0.0;
				for (int n = 0; n < IdfList.size(); n++) {
					word = IdfList.get(n).getWord();
					idf = IdfCalculate(word, pathlist);
					IdfList.get(n).setIdf(idf);
					out.write(word + " " + idf);
					out.write("\r\n");
				}

				out.flush();
				out.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}

	public static double TfCalculate(String word, String filepath) {
		double tf = 0.0;
		int count = 0;
		int wordcount = 0;
		String[] words = new String[] {};
		File f = new File(filepath);
		try {
			InputStreamReader reader = new InputStreamReader(
					new FileInputStream(f));
			BufferedReader br = new BufferedReader(reader);// 建立一个对象,把文件内容转换成计算机能都读懂的语言

			String str = null;
			str = br.readLine();
			while (str != null) {
				words = str.split(" ");
				for (int i = 0; i < words.length; i++) {
					if (word.equals(words[i]))
						wordcount++;
					count++;
				}
				str = br.readLine();
			}
			br.close();
			if (count != 0)
				tf = (double) wordcount / count;
			else
				tf = 0;
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return tf;
	}

	public static void TfListCalculate(String filepath, ArrayList<Tf> TfList) {
		try {
			File words = new File(filepath);
			InputStreamReader wordreader = new InputStreamReader(
					new FileInputStream(words));
			BufferedReader wordsbr = new BufferedReader(wordreader);

			String[] wordslist = new String[] {};
			String str = wordsbr.readLine();

			while (str != null) {
				Matcher m = patPunc.matcher(str);
				str = m.replaceAll("").trim();
				wordslist = str.split(" ");
				for (int i = 0; i < wordslist.length; i++) {
					if (TfList.contains(new Tf(wordslist[i]))
							|| wordslist[i].equals(""))
						continue;
					else
						TfList.add(new Tf(wordslist[i]));
				}
				str = wordsbr.readLine();
			}

			double tf = 0.0;
			for (int j = 0; j < TfList.size(); j++) {
				tf = TfCalculate(TfList.get(j).getWord(), filepath);
				TfList.get(j).setTf(tf);
			}

			wordsbr.close();

		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public static void WeightCalculate(ArrayList<Idf> IdfList,
			ArrayList<Tf> TfList, ArrayList<Weight> WeightList,
			String parentname, String filename) {
		String resultpath = "F:\\数字内容安全\\实验四\\权重计算\\体育分类训练文档\\" + parentname
				+ "\\";
		File resultparent = new File(resultpath);
		if (!resultparent.exists())
			resultparent.mkdir();
		File result = new File(resultpath + filename);
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(result));
			double weight = 0.0;
			int temp = 0;
			for (int i = 0; i < TfList.size(); i++) {
				if ((temp = IdfList
						.lastIndexOf(new Idf(TfList.get(i).getWord()))) != -1) {
					weight = IdfList.get(temp).getIdf() * TfList.get(i).getTf();
					WeightList.add(new Weight(TfList.get(i).getWord(), weight));
					out.write(TfList.get(i).getWord() + " : " + weight);
					out.write("\r\n");
				}
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}

public class WeightCalculateTest {
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
		Date StartDate = new Date();
		Calendar StartCal = Calendar.getInstance();
		DateFormat StartDf = DateFormat.getTimeInstance();
		String StartTime = StartDf.format(StartDate);
		System.out.println("开始时间: " + StartDate);

		String path = "F:\\数字内容安全\\实验四\\分词结果\\体育分类训练文档\\";
		String filepath = null;

		ArrayList<Idf> IdfList = new ArrayList<Idf>();
		ArrayList<Tf> TfList = new ArrayList<Tf>();
		ArrayList<Weight> WeightList = new ArrayList<Weight>();
		ArrayList<String> pathlist = new ArrayList<String>();
		WeightCalculateTest WCT = new WeightCalculateTest();

		File f = new File(path);
		WCT.isDictionary(f, pathlist);

		WeightCompute.IdfListCalculate(pathlist, IdfList);
		Date IdfDate = new Date();
		Calendar IdfCal = Calendar.getInstance();
		DateFormat IdfDf = DateFormat.getTimeInstance();
		String IdfTime = IdfDf.format(IdfDate);
		System.out.println(IdfDate + ": idf值计算完毕");

		for (int i = 0; i < pathlist.size(); i++) {
			File file = new File(pathlist.get(i));
			filepath = file.getAbsolutePath();
			String parentname = file.getParentFile().getName();
			String filename = file.getName();
			WeightCompute.TfListCalculate(filepath, TfList);
			Date TfDate = new Date();
			Calendar TfCal = Calendar.getInstance();
			DateFormat TfDf = DateFormat.getTimeInstance();
			String TfTime = IdfDf.format(TfDate);
			System.out.println(TfDate + ": " + parentname + "\\" + filename
					+ " tf值计算完毕");

			WeightCompute.WeightCalculate(IdfList, TfList, WeightList,
					parentname, filename);
			Date WeightDate = new Date();
			Calendar WeightCal = Calendar.getInstance();
			DateFormat WeightDf = DateFormat.getTimeInstance();
			String WeightTime = IdfDf.format(WeightDate);
			System.out.println(WeightDate + ": " + parentname + "\\" + filename
					+ " weight值计算完毕");
			TfList.clear();
			WeightList.clear();
		}
	}
}
