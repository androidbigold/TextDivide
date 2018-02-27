package TextFiltering;

import java.io.File;
import java.util.ArrayList;

public class Mainly {
	public static void main(String[] args) {
		String Exercisepath = "F:\\数字内容安全\\实验四\\权重计算\\体育分类训练文档\\";
		String Testpath = "F:\\数字内容安全\\实验四\\权重计算\\体育分类测试文档\\";
		String savepath = "F:\\数字内容安全\\实验五\\分类结果\\体育分类测试文档\\";

		FeatureSelection fs = new FeatureSelection();
		ArrayList<String> pathlist = new ArrayList<String>();
		ArrayList<String> sortlist = new ArrayList<String>();
		ArrayList<Integer> sortdocnumber = new ArrayList<Integer>();
		ArrayList<String> resultpathlist = new ArrayList<String>();
		ArrayList<String> resultsortlist = new ArrayList<String>();
		ArrayList<Integer> resultsortdocnumber = new ArrayList<Integer>();

		File f = new File(Exercisepath);
		fs.isDictionary(f, pathlist);
		fs.FileSort(pathlist, sortlist, sortdocnumber);

		DividerList[] dl = new DividerList[sortlist.size()];
		dl = fs.CalculateDF(pathlist, sortlist, sortdocnumber);

		pathlist.clear();
		File testfile = new File(Testpath);
		fs.isDictionary(testfile, pathlist);

		TextSort ts = new TextSort();
		int sort = 0;
		int[] rightsort = new int[sortlist.size()];
		int[] sortnumber = new int[sortlist.size()];
		String saveplace = null;

		for (int i = 0; i < pathlist.size(); i++) {
			File tf = new File(pathlist.get(i));
			sort = ts.SortCalculate(tf, dl, sortlist);
			sortnumber[sort]++;
			if (tf.getParentFile().getName().equals(sortlist.get(sort)))
				rightsort[sort]++;
			saveplace = savepath + sortlist.get(sort);
			f = new File(saveplace);
			if (!f.exists())
				f.mkdirs();
			ts.MoveFile(tf, saveplace + "\\");
		}

		sortlist.clear();
		sortdocnumber.clear();
		fs.FileSort(pathlist, sortlist, sortdocnumber);
		
		f = new File(savepath);
		fs.isDictionary(f, resultpathlist);
		fs.FileSort(resultpathlist, resultsortlist, resultsortdocnumber);

		int[] rightcount = new int[resultsortlist.size()];
		double rightpercentsum = 0.0;
		double callbackpercentsum = 0.0;

		File result = null;
		String parentname = null;
		int resultsort = 0;
		int find = 0;
		for (int j = 0; j < resultpathlist.size(); j++) {
			result = new File(resultpathlist.get(j));
			parentname = result.getParentFile().getName();
			resultsort = sortlist.indexOf(parentname);
			if (resultsort != -1) {
				find = pathlist.indexOf(Testpath + parentname + "\\"
						+ result.getName());
				if (find != -1)
					rightcount[resultsortlist.indexOf(parentname)]++;
			}
		}

		int temp = 0;
		for (int j = 0; j < resultsortlist.size(); j++) {
			if ((temp = sortlist.indexOf(resultsortlist.get(j))) != -1) {
				if (resultsortdocnumber.get(j) != 0) {
					rightpercentsum += (double) rightcount[j]
							/ resultsortdocnumber.get(j);
					System.out.println(resultsortlist.get(j) + "准确率: "
							+ (double) rightcount[j]
							/ resultsortdocnumber.get(j));
				}
				if (sortdocnumber.get(temp) != 0) {
					callbackpercentsum += (double) rightcount[j]
							/ sortdocnumber.get(temp);
					System.out.println(resultsortlist.get(j) + "召回率: "
							+ (double) rightcount[j] / sortdocnumber.get(temp));
				}
				System.out.println();
			}
		}
	}
}
