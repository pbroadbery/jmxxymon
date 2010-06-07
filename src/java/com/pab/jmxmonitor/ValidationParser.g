parser grammar ValidationParser;

options {
  language = Java;
  tokenVocab = Javalex;
}

@header { 
package com.pab.jmxmonitor; 
import java.util.List; 
import java.util.LinkedList; 
import java.util.Collections; 
}

whole returns [Validator v]: p=parenexpr { $v=p; } EOF;

parenexpr returns [Validator value ]: v=var { $value = Env.construct((Validator.Constructor) v, Collections.emptyList());}
  | (v=var OPAREN cl=commalist CPAREN) { $value = Env.construct((Validator.Constructor) v, cl);}
  | (v=var OPAREN CPAREN) { $value = Env.construct((Validator.Constructor) v, Collections.emptyList()) ;};
   
commalist returns [ List<Object> l]: e1=expr { l=new LinkedList<Object>(); $l.add(e1);} (COMMA e2=expr { $l.add(e2); })*;

expr returns [ Object value ]:
    s=STRING {String tmp = $s.text; tmp = tmp.substring(1, tmp.length()-1); $value=tmp;} 
    | i=INTEGER { $value=Integer.parseInt($i.text);}
    | p=parenexpr { $value=p;};
   
var returns [ Object value]: ID {$value = Env.lookupLocalId($ID.text);}
  | ( id1=ID  DOT id2=ID) {$value = Env.lookupId($id1.text, $id2.text); };
