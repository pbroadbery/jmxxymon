// $ANTLR 3.2 Sep 23, 2009 12:02:23 /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g 2010-03-02 22:10:20
 
package com.pab.jmxmonitor; 
import java.util.List; 
import java.util.LinkedList; 
import java.util.Collections; 


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class ValidationParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE", "WS", "COMMENT", "ID", "OPAREN", "CPAREN", "STRING", "INTEGER", "COMMA", "DOT"
    };
    public static final int INTEGER=11;
    public static final int WS=5;
    public static final int RULE=4;
    public static final int OPAREN=8;
    public static final int COMMA=12;
    public static final int CPAREN=9;
    public static final int ID=7;
    public static final int COMMENT=6;
    public static final int DOT=13;
    public static final int EOF=-1;
    public static final int STRING=10;

    // delegates
    // delegators


        public ValidationParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public ValidationParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return ValidationParser.tokenNames; }
    public String getGrammarFileName() { return "/home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g"; }



    // $ANTLR start "whole"
    // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:15:1: whole returns [Validator v] : p= parenexpr EOF ;
    public final Validator whole() throws RecognitionException {
        Validator v = null;

        Validator p = null;


        try {
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:15:28: (p= parenexpr EOF )
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:15:30: p= parenexpr EOF
            {
            pushFollow(FOLLOW_parenexpr_in_whole46);
            p=parenexpr();

            state._fsp--;

             v =p; 
            match(input,EOF,FOLLOW_EOF_in_whole50); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return v;
    }
    // $ANTLR end "whole"


    // $ANTLR start "parenexpr"
    // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:17:1: parenexpr returns [Validator value ] : (v= var | (v= var OPAREN cl= commalist CPAREN ) | (v= var OPAREN CPAREN ) );
    public final Validator parenexpr() throws RecognitionException {
        Validator value = null;

        Object v = null;

        List<Object> cl = null;


        try {
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:17:37: (v= var | (v= var OPAREN cl= commalist CPAREN ) | (v= var OPAREN CPAREN ) )
            int alt1=3;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==ID) ) {
                switch ( input.LA(2) ) {
                case DOT:
                    {
                    int LA1_2 = input.LA(3);

                    if ( (LA1_2==ID) ) {
                        int LA1_5 = input.LA(4);

                        if ( (LA1_5==OPAREN) ) {
                            int LA1_4 = input.LA(5);

                            if ( (LA1_4==CPAREN) ) {
                                alt1=3;
                            }
                            else if ( (LA1_4==ID||(LA1_4>=STRING && LA1_4<=INTEGER)) ) {
                                alt1=2;
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 1, 4, input);

                                throw nvae;
                            }
                        }
                        else if ( (LA1_5==EOF||LA1_5==CPAREN||LA1_5==COMMA) ) {
                            alt1=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 1, 5, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 1, 2, input);

                        throw nvae;
                    }
                    }
                    break;
                case EOF:
                case CPAREN:
                case COMMA:
                    {
                    alt1=1;
                    }
                    break;
                case OPAREN:
                    {
                    int LA1_4 = input.LA(3);

                    if ( (LA1_4==CPAREN) ) {
                        alt1=3;
                    }
                    else if ( (LA1_4==ID||(LA1_4>=STRING && LA1_4<=INTEGER)) ) {
                        alt1=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 1, 4, input);

                        throw nvae;
                    }
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 1, 1, input);

                    throw nvae;
                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:17:39: v= var
                    {
                    pushFollow(FOLLOW_var_in_parenexpr63);
                    v=var();

                    state._fsp--;

                     value = Env.construct((Validator.Constructor) v, Collections.emptyList());

                    }
                    break;
                case 2 :
                    // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:18:5: (v= var OPAREN cl= commalist CPAREN )
                    {
                    // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:18:5: (v= var OPAREN cl= commalist CPAREN )
                    // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:18:6: v= var OPAREN cl= commalist CPAREN
                    {
                    pushFollow(FOLLOW_var_in_parenexpr74);
                    v=var();

                    state._fsp--;

                    match(input,OPAREN,FOLLOW_OPAREN_in_parenexpr76); 
                    pushFollow(FOLLOW_commalist_in_parenexpr80);
                    cl=commalist();

                    state._fsp--;

                    match(input,CPAREN,FOLLOW_CPAREN_in_parenexpr82); 

                    }

                     value = Env.construct((Validator.Constructor) v, cl);

                    }
                    break;
                case 3 :
                    // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:19:5: (v= var OPAREN CPAREN )
                    {
                    // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:19:5: (v= var OPAREN CPAREN )
                    // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:19:6: v= var OPAREN CPAREN
                    {
                    pushFollow(FOLLOW_var_in_parenexpr94);
                    v=var();

                    state._fsp--;

                    match(input,OPAREN,FOLLOW_OPAREN_in_parenexpr96); 
                    match(input,CPAREN,FOLLOW_CPAREN_in_parenexpr98); 

                    }

                     value = Env.construct((Validator.Constructor) v, Collections.emptyList()) ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "parenexpr"


    // $ANTLR start "commalist"
    // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:21:1: commalist returns [ List<Object> l] : e1= expr ( COMMA e2= expr )* ;
    public final List<Object> commalist() throws RecognitionException {
        List<Object> l = null;

        Object e1 = null;

        Object e2 = null;


        try {
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:21:36: (e1= expr ( COMMA e2= expr )* )
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:21:38: e1= expr ( COMMA e2= expr )*
            {
            pushFollow(FOLLOW_expr_in_commalist117);
            e1=expr();

            state._fsp--;

             l=new LinkedList<Object>(); l.add(e1);
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:21:89: ( COMMA e2= expr )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==COMMA) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:21:90: COMMA e2= expr
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_commalist122); 
            	    pushFollow(FOLLOW_expr_in_commalist126);
            	    e2=expr();

            	    state._fsp--;

            	     l.add(e2); 

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return l;
    }
    // $ANTLR end "commalist"


    // $ANTLR start "expr"
    // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:23:1: expr returns [ Object value ] : (s= STRING | i= INTEGER | p= parenexpr );
    public final Object expr() throws RecognitionException {
        Object value = null;

        Token s=null;
        Token i=null;
        Validator p = null;


        try {
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:23:30: (s= STRING | i= INTEGER | p= parenexpr )
            int alt3=3;
            switch ( input.LA(1) ) {
            case STRING:
                {
                alt3=1;
                }
                break;
            case INTEGER:
                {
                alt3=2;
                }
                break;
            case ID:
                {
                alt3=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }

            switch (alt3) {
                case 1 :
                    // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:24:5: s= STRING
                    {
                    s=(Token)match(input,STRING,FOLLOW_STRING_in_expr147); 
                    value =(s!=null?s.getText():null);

                    }
                    break;
                case 2 :
                    // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:25:7: i= INTEGER
                    {
                    i=(Token)match(input,INTEGER,FOLLOW_INTEGER_in_expr160); 
                     value =Integer.parseInt((i!=null?i.getText():null));

                    }
                    break;
                case 3 :
                    // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:26:7: p= parenexpr
                    {
                    pushFollow(FOLLOW_parenexpr_in_expr172);
                    p=parenexpr();

                    state._fsp--;

                     value =p;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "expr"


    // $ANTLR start "var"
    // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:28:1: var returns [ Object value] : ( ID | (id1= ID DOT id2= ID ) );
    public final Object var() throws RecognitionException {
        Object value = null;

        Token id1=null;
        Token id2=null;
        Token ID1=null;

        try {
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:28:28: ( ID | (id1= ID DOT id2= ID ) )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==ID) ) {
                int LA4_1 = input.LA(2);

                if ( (LA4_1==DOT) ) {
                    alt4=2;
                }
                else if ( (LA4_1==EOF||(LA4_1>=OPAREN && LA4_1<=CPAREN)||LA4_1==COMMA) ) {
                    alt4=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:28:30: ID
                    {
                    ID1=(Token)match(input,ID,FOLLOW_ID_in_var188); 
                    value = Env.lookupLocalId((ID1!=null?ID1.getText():null));

                    }
                    break;
                case 2 :
                    // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:29:5: (id1= ID DOT id2= ID )
                    {
                    // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:29:5: (id1= ID DOT id2= ID )
                    // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/ValidationParser.g:29:7: id1= ID DOT id2= ID
                    {
                    id1=(Token)match(input,ID,FOLLOW_ID_in_var200); 
                    match(input,DOT,FOLLOW_DOT_in_var203); 
                    id2=(Token)match(input,ID,FOLLOW_ID_in_var207); 

                    }

                    value = Env.lookupId((id1!=null?id1.getText():null), (id2!=null?id2.getText():null)); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "var"

    // Delegated rules


 

    public static final BitSet FOLLOW_parenexpr_in_whole46 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_whole50 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_var_in_parenexpr63 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_var_in_parenexpr74 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_OPAREN_in_parenexpr76 = new BitSet(new long[]{0x0000000000000C80L});
    public static final BitSet FOLLOW_commalist_in_parenexpr80 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_CPAREN_in_parenexpr82 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_var_in_parenexpr94 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_OPAREN_in_parenexpr96 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_CPAREN_in_parenexpr98 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expr_in_commalist117 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_COMMA_in_commalist122 = new BitSet(new long[]{0x0000000000000C80L});
    public static final BitSet FOLLOW_expr_in_commalist126 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_STRING_in_expr147 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTEGER_in_expr160 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parenexpr_in_expr172 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_var188 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_var200 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_DOT_in_var203 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ID_in_var207 = new BitSet(new long[]{0x0000000000000002L});

}