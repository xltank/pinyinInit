package org.rmt.lucene.analyzer.plugin;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;


class Pinyin
{
  private HanyuPinyinOutputFormat format = null;
  private String[] pinyin;

  public Pinyin()
  {
    this.format = new HanyuPinyinOutputFormat();
    this.format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
    this.format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    this.format.setVCharType(HanyuPinyinVCharType.WITH_V);
  }

  public String getPinyinInitial(char c)
  {
    try
    {
      this.pinyin = PinyinHelper.toHanyuPinyinStringArray(c, this.format);
    }
    catch (BadHanyuPinyinOutputFormatCombination e)
    {
      e.printStackTrace();
    }
    if (this.pinyin == null) { return null;
    }
    String rtn = this.pinyin[0];
    return rtn;
  }
}
