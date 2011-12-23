//
// $Id: Lexicon.java,v 1.2 2006/02/16 18:15:02 perera Exp $
//
// Durm German Lemmatization System
// http://www.ipd.uka.de/~durm/tm/lemma/
//
//
// Interface to the lexicon. Defines the methods
// addWord(): Insert an entry to the lexicon
// getLemma(): Get the lemma for a given word from the lexicon 
// getGender(): returns the gender for a given word
// getCase(): returns the case for a given word 
// updateLexicon(): updates the lexicon
//
// (c) 2005--2006 University of Karlsruhe, IPD,
//                Praharshana Perera
//
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
package com.zuehlke.analysis;


public interface Lexicon
{
   public String getLemma(String word);
   
   public String getNumber(String word);
   
   public String getGender(String word);
   
   public String getCase(String word);
}