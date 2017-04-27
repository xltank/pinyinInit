package org.rmt.lucene.analyzer.plugin;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Stack;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeSource.State;

/**
 * // https://lucene.apache.org/core/4_7_0/core/org/apache/lucene/analysis/TokenStream.html
 * */
public class HanyuPinyinFilter
		extends TokenFilter {
	private CharTermAttribute cta = null;
	private PositionIncrementAttribute pia = null;
	private OffsetAttribute osa = null;
	private TypeAttribute typ = null;
	private State cursor;
	private Stack words = null;
	private StringBuilder abbr;
	private StringBuilder cat;
	private boolean added;

	public HanyuPinyinFilter(TokenStream stream) {
		super(stream);
		this.cta = ((CharTermAttribute) addAttribute(CharTermAttribute.class));
		this.pia = ((PositionIncrementAttribute) addAttribute(PositionIncrementAttribute.class));
		this.osa = ((OffsetAttribute) addAttribute(OffsetAttribute.class));
		this.typ = ((TypeAttribute) addAttribute(TypeAttribute.class));
		this.words = new Stack();
		this.abbr = new StringBuilder();
		this.cat = new StringBuilder();
	}

	/**
	 cta is: 科
	 cta is: 幻
	 cta is: 飞
	 cta is: 行
	 cta is: 器
	 word is: kehuanfeixingqi
	 word is: khfxq
	 word is: qi
	 word is: xing
	 word is: fei
	 word is: huan
	 word is: ke
	 **/

	/**
	 ** incrementToken **
	 public abstract boolean incrementToken()
	 throws IOException

	 Consumers (i.e., IndexWriter) use this method to advance the stream to the next token. Implementing classes must implement this method and update the appropriate AttributeImpls with the attributes of the next token.
	 The producer must make no assumptions about the attributes after the method has been returned: the caller may arbitrarily change it. If the producer needs to preserve the state for subsequent calls, it can use AttributeSource.captureState() to create a copy of the current attribute state.

	 This method is called for every token of a document, so an efficient implementation is crucial for good performance. To avoid calls to AttributeSource.addAttribute(Class) and AttributeSource.getAttribute(Class), references to all AttributeImpls that this stream uses should be retrieved during instantiation.

	 To ensure that filters and consumers know which attributes are available, the attributes must be added during instantiation. Filters and consumers are not required to check for availability of attributes in incrementToken().

	 Returns:
	 false for end of stream; true otherwise
	 Throws:
	 IOException
	 * */

	public boolean incrementToken()
			throws IOException {
		if (!this.input.incrementToken()) {
			if ((!this.added) && (this.words.size() > 0)) {
				this.words.push(this.abbr.toString());
				this.words.push(this.cat.toString());
				this.added = true;
				this.abbr = new StringBuilder();
				this.cat = new StringBuilder();
			}

			if (this.words.size() > 0) {
				String str = this.words.pop().toString();
				System.out.println("word is: " + str);
				restoreState(this.cursor);
				this.cta.setEmpty();

				this.cta.append(str);
				this.pia.setPositionIncrement(this.pia.getPositionIncrement());
				this.osa.setOffset(this.osa.endOffset() + 1, this.osa.endOffset() + str.length());
				this.typ.setType("word");
				this.cursor = captureState();
				return true;
			}
			return false;
		}


		System.out.println("cta is: " + this.cta.toString());
		this.added = false;
		makePinyinInitial(this.cta.toString());
		this.cursor = captureState();
		return true;
	}

	private boolean makePinyinInitial(String str) {
		Pinyin pinyin = new Pinyin();
		String tmp = null;
		for (int i = 0; i < str.length(); i++) {
			tmp = pinyin.getPinyinInitial(str.charAt(i));
			if (tmp != null) {
				char initial = tmp.charAt(0);


				this.abbr.append(initial);
				this.cat.append(tmp);
				this.words.push(tmp);
			}
		}
		return true;
	}

	public void close() {

	}

	public void end() {

	}

	public void reset() {

	}
}