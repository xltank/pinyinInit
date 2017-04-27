package org.rmt.lucene.analyzer.plugin;

import java.util.Map;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;


public class HanyuPinyinFilterFactory
  extends TokenFilterFactory
{
  public HanyuPinyinFilterFactory(Map<String, String> args)
  {
    super(args);
    if (!args.isEmpty()) {
      throw new IllegalArgumentException("Unknown parameters: " + args);
    }
  }

  public TokenFilter create(TokenStream input)
  {
    return new HanyuPinyinFilter(input);
  }
}
