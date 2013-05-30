package jp.linqdroid.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.Iterator;

import jp.linqdroid.Disposable;
import jp.linqdroid.Enumerable;
import jp.linqdroid.F;

public class TextStreamingReader {

	public static TextStreamingReader load(final String fileName) {
		return new TextStreamingReader(new F<Reader>() {
			@Override
			public Reader invoke() {
				try {
					return new FileReader(fileName);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				return null; 
			}
		});
	}
	public static TextStreamingReader load(final URL url) {
		return new TextStreamingReader(new F<Reader>() {
			@Override
			public Reader invoke() {
				try {
					return new InputStreamReader(url.openStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}
	public static TextStreamingReader parse(final String xmlString)  {
		return new TextStreamingReader(new F<Reader>() {
			@Override
			public Reader invoke() {
				return  new StringReader(xmlString);
			}
		});
	} 
	
	public static TextStreamingReader load(final InputStream stream) {
		return new TextStreamingReader(new F<Reader>() {
			@Override
			public Reader invoke() {
				return new InputStreamReader(stream);
			}
		});
	}

	/** 元となるストリームを作成するファンクタ */
	private F<Reader> readerFactory;
	/** 列挙完了時にストリームを閉じるファンクタ */
	private Disposable disposer;
	private BufferedReader reader;

	private TextStreamingReader(F<Reader> readerFactory) {
		this.readerFactory = readerFactory;
	}

	public Enumerable<String> getLines() {
		reader = new BufferedReader(this.readerFactory.invoke());
		//列挙完了時にストリームを閉じるためのDisposableを作成
		this.disposer = new Disposable() {
			@Override
			public void dispose() {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		return Enumerable.from(new Iterator<String>() {
			@Override
			public boolean hasNext() {
				try {
					return reader.ready();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}

			@Override
			public String next() {
				try {
					return reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		}, disposer);
	}
}

