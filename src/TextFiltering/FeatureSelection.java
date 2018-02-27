package TextFiltering;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

class DF {
	private String word = null;
	private int count = 0;

	public DF(String word) {
		this.word = word;
	}

	public DF() {

	}

	public String GetWord() {
		return word;
	}

	public void count() {
		count++;
	}

	public int GetCount() {
		return count;
	}

	public boolean equals(Object otherObject) {
		if (this == otherObject)
			return true;
		if (otherObject == null)
			return false;
		if (getClass() != otherObject.getClass())
			return false;
		DF other = (DF) otherObject;
		return word.equals(other.GetWord());
	}
}

class Divider {
	private String word = null;
	private double pwc = 0.0;

	public Divider(String word) {
		this.word = word;
	}

	public Divider(String word, double pwc) {
		this.word = word;
		this.pwc = pwc;
	}

	public void SetPwc(double pwc) {
		this.pwc = pwc;
	}

	public String GetWord() {
		return word;
	}

	public double GetPwc() {
		return pwc;
	}

	public boolean equals(Object otherObject) {
		if (this == otherObject)
			return true;
		if (otherObject == null)
			return false;
		if (getClass() != otherObject.getClass())
			return false;
		Divider other = (Divider) otherObject;
		return word.equals(other.GetWord());
	}

}

class DividerList {
	private ArrayList<Divider> dl = null;
	private double pc = 0.0;

	public DividerList() {
		this.dl = new ArrayList<Divider>();
	}

	public ArrayList<Divider> GetDividerList() {
		return dl;
	}

	public void SetPc(double pc) {
		this.pc = pc;
	}

	public double GetPc() {
		return pc;
	}
}

public class FeatureSelection {
	public DividerList[] CalculateDF(ArrayList<String> pathlist,
			ArrayList<String> sortlist, ArrayList<Integer> sortdocnumber) {
		int doccount = 0;
		int currentsort = 0;
		int docnumbersum = pathlist.size();
		ArrayList<DF> DFList = new ArrayList<DF>();
		DividerList[] dl = new DividerList[sortlist.size()];
		for (int n = 0; n < sortlist.size(); n++)
			dl[n] = new DividerList();
		String savepath = "F:\\数字内容安全\\实验五\\特征选择\\体育分类训练文档\\";

		for (int i = 0; i < sortlist.size(); i++) {
			File result = new File(savepath + sortlist.get(i) + ".txt");

			for (int j = currentsort; j < pathlist.size(); j++) {
				File file = new File(pathlist.get(j));
				if (!file.getParentFile().getName().equals(sortlist.get(i))) {
					try {
						result.createNewFile();
						BufferedWriter input = new BufferedWriter(
								new FileWriter(result));

						String word = null;
						int dfi = 0;
						double pwc = 0.0;
						DF temp = new DF();
						ArrayList<Divider> dividerlist = dl[i].GetDividerList();
						dl[i].SetPc((double) doccount / pathlist.size());

						for (int k = 0; k < DFList.size(); k++) {
							word = DFList.get(k).GetWord();
							dfi = DFList.get(k).GetCount();
							if (dfi > 0.4 * doccount && dfi < 0.8 * doccount) {
								pwc = (double) (dfi + 1)
										/ (doccount + DFList.size());
								dividerlist.add(new Divider(word, pwc));
								input.write(word + " " + dfi);
								input.write("\r\n");
							}
						}

						input.flush();
						input.close();
					} catch (IOException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
					Date FinishDate = new Date();
					Calendar FinishCal = Calendar.getInstance();
					DateFormat FinishDf = DateFormat.getTimeInstance();
					String FinishTime = FinishDf.format(FinishDate);
					System.out.println(FinishDate);
					System.out.println(sortlist.get(i) + ": 特征选择完毕");
					System.out.println();
					DFList.clear();
					doccount = 0;
					currentsort = j;
					break;
				} else {
					try {
						InputStreamReader reader = new InputStreamReader(
								new FileInputStream(file));
						BufferedReader br = new BufferedReader(reader);// 建立一个对象,把文件内容转换成计算机能都读懂的语言

						String str = br.readLine();
						int temp = 0;
						int n = 0;
						String word = null;

						while (str != null) {
							temp = str.indexOf(" ");
							word = str.substring(0, temp);
							if (word.length() != 1) {
								if ((n = DFList.indexOf(new DF(word))) != -1)
									DFList.get(n).count();
								else {
									DFList.add(new DF(word));
									DFList.get(DFList.size() - 1).count();
								}
							}
							str = br.readLine();
						}
						doccount++;
						br.close();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}// 建立一个输入流对象
					if (j == pathlist.size() - 1) {
						try {
							result.createNewFile();
							BufferedWriter input = new BufferedWriter(
									new FileWriter(result));

							String word = null;
							int dfi = 0;
							double pwc = 0.0;
							DF temp = new DF();
							ArrayList<Divider> dividerlist = dl[i]
									.GetDividerList();
							dl[i].SetPc((double) doccount / pathlist.size());

							for (int k = 0; k < DFList.size(); k++) {
								word = DFList.get(k).GetWord();
								dfi = DFList.get(k).GetCount();
								if (dfi > 0.4 * doccount
										&& dfi < 0.8 * doccount) {
									pwc = (double) (dfi + 1)
											/ (doccount + DFList.size());
									dividerlist.add(new Divider(word, pwc));
									input.write(word + " " + dfi);
									input.write("\r\n");
								}
							}

							input.flush();
							input.close();
						} catch (IOException e1) {
							// TODO 自动生成的 catch 块
							e1.printStackTrace();
						}
						Date FinishDate = new Date();
						Calendar FinishCal = Calendar.getInstance();
						DateFormat FinishDf = DateFormat.getTimeInstance();
						String FinishTime = FinishDf.format(FinishDate);
						System.out.println(FinishDate);
						System.out.println(sortlist.get(i) + ": 特征选择完毕");
						System.out.println();
					}
				}
			}
		}
		return dl;
	}

	public void isDictionary(File f, ArrayList<String> pathlist) {
		// 判断传入对象是否为一个文件夹对象
		File[] t = f.listFiles();
		for (int i = 0; i < t.length; i++) {
			if (t[i].isDirectory())
				isDictionary(t[i], pathlist);
			else {
				try {
					if (!t[i].getName().equals("idf.txt"))
						pathlist.add(t[i].getCanonicalPath());
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}
	}

	public void FileSort(ArrayList<String> pathlist,
			ArrayList<String> sortlist, ArrayList<Integer> sortdocnumber) {
		String parentname = null;
		int temp = 0;
		for (int i = 0; i < pathlist.size(); i++) {
			File f = new File(pathlist.get(i));
			parentname = f.getParentFile().getName();
			if (!sortlist.contains(parentname)) {
				sortlist.add(parentname);
				if (i != 0)
					sortdocnumber.add(temp);
				temp = 1;
			} else if (i == pathlist.size() - 1) {
				temp++;
				sortdocnumber.add(temp);
			} else
				temp++;
		}
	}
}