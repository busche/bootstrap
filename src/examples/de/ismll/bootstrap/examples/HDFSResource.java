package de.ismll.bootstrap.examples;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.fs.FileSystem;
//import org.apache.hadoop.fs.Path;

import de.ismll.bootstrap.CommandLineParser;

public class HDFSResource {

//	static FileSystem fs = null;
//	private final Path mHdfsPath;
//
//	public HDFSResource(Path hdfsPath) {
//		this.mHdfsPath = hdfsPath;
//	}
//	
//	static {
//		Configuration conf = new Configuration();
//		try {
//			conf.addResource(new URL(
//					"file:///share/apps/hadoop/conf/core-site.xml"));
//		} catch (MalformedURLException e1) {
//			e1.printStackTrace();
//			throw new RuntimeException(e1);
//		}
//		System.out.println();
//		
//		try {
//			fs = FileSystem.get(conf);
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.exit(1);
//		}
//
//	}
//	
//	public static HDFSResource convert(Object source) {
//		URI reference = (URI) CommandLineParser.convert(source, URI.class);
//		Path hdfsPath = new Path(reference);
//		
//		return new HDFSResource(hdfsPath);
//	}
//	
//
//	public InputStream openStream() throws IOException {
//		return fs.open(mHdfsPath);
//	}
//
//	public Path getmHdfsPath() {
//		return mHdfsPath;
//	}
}
