package TextFiltering;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TextSort {
	public int SortCalculate(File f, DividerList[] dl,
			ArrayList<String> sortlist) {
		int sort = 0;
		try {
			int temp = 0;
			double[] pc = new double[dl.length];
			double[] pw = new double[dl.length];
			for (int i = 0; i < dl.length; i++) {
				pc[i] = dl[i].GetPc();
				pw[i] = 1.0;
			}

			InputStreamReader reader = new InputStreamReader(
					new FileInputStream(f));
			BufferedReader br = new BufferedReader(reader);// 建立一个对象,把文件内容转换成计算机能都读懂的语言

			String word = null;
			String str = br.readLine();
			while (str != null) {
				word = str.substring(0, str.indexOf(" "));
				for (int j = 0; j < dl.length; j++) {
					if ((temp = dl[j].GetDividerList().indexOf(
							new Divider(word))) != -1)
						pw[j] *= dl[j].GetDividerList().get(temp).GetPwc() * 100000;
				}
				str = br.readLine();
			}
			br.close();
			double max = pw[0] * pc[0] * 100000;
			for (int k = 1; k < dl.length; k++) {
				if (max < pw[k] * pc[k] * 100000) {
					max = pw[k] * pc[k] * 100000;
					sort = k;
				}
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return sort;
	}

	public void MoveFile(File f, String savepath) {
		try {
			InputStreamReader reader = new InputStreamReader(
					new FileInputStream(f));
			BufferedReader br = new BufferedReader(reader);// 建立一个对象,把文件内容转换成计算机能都读懂的语言

			String filename = f.getName();
			File result = new File(savepath + filename);
			result.createNewFile();
			BufferedWriter input = new BufferedWriter(new FileWriter(result));

			String str = br.readLine();
			while (str != null) {
				input.write(str);
				input.newLine();
				str = br.readLine();
			}
			br.close();
			input.flush();
			input.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

}