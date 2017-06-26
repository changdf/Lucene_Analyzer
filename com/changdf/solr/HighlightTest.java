package com.changdf.solr;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryTermScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.CommandLineUtil;

import net.paoding.analysis.analyzer.PaodingAnalyzer;

public class HighlightTest {

	public static void main(String[] args) throws Exception {
		String indexDir = "C:\\Users\\changdf\\Desktop\\solr-6.5.1\\example\\example-DIH\\solr\\db\\data\\index";
		FileSystem fs = FileSystems.getDefault();
		FileSystem fileSystem = fs.provider().getFileSystem(URI.create("file:///"));
		Path indexPath = fileSystem.getPath(indexDir);
		//Files.createDirectory(indexPath);
		FSDirectory fSDirectory = CommandLineUtil.newFSDirectory(SimpleFSDirectory.class, indexPath);
		
		Query query = new TermQuery(new Term("tag", "程序"));
	    IndexReader reader = DirectoryReader.open(fSDirectory);
	    IndexSearcher searcher = new IndexSearcher(reader);
	    // This scorer can return negative idf -> null fragment
	    Scorer scorer = new QueryTermScorer( query, searcher.getIndexReader(), "tag" );
	    // This scorer doesn't use idf (patch version)
	    //Scorer scorer = new QueryTermScorer( query, "t_text1" );
	    Highlighter h = new Highlighter( scorer );
	    
	    TopDocs hits = searcher.search(query, 10);
	    for( int i = 0; i < hits.scoreDocs.length; i++ ){
	    	Document doc = searcher.doc( hits.scoreDocs[i].doc);
		    String result = h.getBestFragment( new PaodingAnalyzer(), "tag", doc.get( "tag" ));
		    System.out.println("result:" +  result);
	    }
	    /*TopDocs hits = searcher.search(query, reader.maxDoc());
	    for( int i = 0; i < hits.totalHits; i++ ){
	      Document doc = searcher.doc( hits.scoreDocs[i].doc);
	      String result = h.getBestFragment( new PaodingAnalyzer(), "tag", doc.get( "tag" ));
	      System.out.println("result:" +  result);
	    }*/
	    reader.close();
	}

}
