//
// $Id: LexiconEntry.java,v 1.2 2006/02/16 18:15:02 perera Exp $
//
// Durm German Lemmatization System
// http://www.ipd.uka.de/~durm/tm/lemma/
//
//
// Class which defines the attributes of an lexicon entry and extends
// the class Lemma
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


public class LexiconEntry extends Lemma
{
   String noun;
   String lemma;
   String number;
   String gender;
   String gcase;
   String freq;
   String insertedOn;
   String lastModified;
   String pointer;
   String locked;
   
   public String toString()
   {
     return noun+"\t"+number+"\t"+gender+"\t"+gcase+"\t"+lemma+"\t"+freq+"\t"+insertedOn+"\t"+lastModified+"\t"+pointer+"\t"+locked;
   }
 
}