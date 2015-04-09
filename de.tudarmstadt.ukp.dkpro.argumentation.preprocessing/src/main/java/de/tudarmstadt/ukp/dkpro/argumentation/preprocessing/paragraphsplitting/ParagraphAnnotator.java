/*
 * Copyright 2014
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.tudarmstadt.ukp.dkpro.argumentation.preprocessing.paragraphsplitting;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Paragraph;


/**
 * Annotator for paragraphs in text (specifically suited for essays in essayforum.com)
 * Expected format of input text:-
 * <Topic>\n\n<Paragraph1 Sentences....>\n<Paragraph2 Sentences....>......
 * 
 * @author perumal, paliwal, stab
 * 
 */
public class ParagraphAnnotator extends JCasAnnotator_ImplBase {
    
    @Override
    public void process(JCas jCas)
        throws AnalysisEngineProcessException
    {
    	String text = jCas.getDocumentText();
    	ArrayList<ParagraphBoundaries> boundaries = getParagraphBoundaries(text);
    	Iterator<ParagraphBoundaries> it = boundaries.iterator();
    	while(it.hasNext()) {
    		ParagraphBoundaries p = it.next();
    		//System.out.println(p.getBegin() + " " + p.getEnd());
    		new Paragraph(jCas, p.getBegin(), p.getEnd()).addToIndexes();
    	}
    }
    
    public ArrayList<ParagraphBoundaries> getParagraphBoundaries(String text) {
    	ArrayList<ParagraphBoundaries> list = new ArrayList<ParagraphBoundaries>();
    	ParagraphBoundaries boundaries = null;
    	int begin = 0;
    	int end = 0;
    	int cursor = 0;
    	while(text.charAt(cursor) != '\n') {
			cursor++;
		}
    	begin = cursor + 2;
    	cursor += 2;
    	while(cursor < text.length()) {
    		while(cursor < text.length() && text.charAt(cursor) != '\n') {
    			cursor++;
    		}
    		end = cursor;
    		boundaries = new ParagraphBoundaries();
    		boundaries.setBegin(begin);
    		boundaries.setEnd(end);
    		//System.out.println(begin + " " + end);
    		list.add(boundaries);
    		begin = end + 1;
    		cursor++;
    	}
    	return list;
    }
    
    public class ParagraphBoundaries {
    	int begin;
    	int end;
    	public int getBegin() {
    		return begin;
    	}
    	public void setBegin(int begin) {
    		this.begin = begin;
    	}
    	public int getEnd() {
    		return end;
    	}
    	public void setEnd(int end) {
    		this.end = end;
    	}
    }
}