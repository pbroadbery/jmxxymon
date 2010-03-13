// $ANTLR 3.2 Sep 23, 2009 12:02:23 /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g 2010-03-02 22:10:18
package com.pab.jmxmonitor; 

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class Javalex extends Lexer {
    public static final int INTEGER=11;
    public static final int WS=5;
    public static final int RULE=4;
    public static final int COMMA=12;
    public static final int OPAREN=8;
    public static final int CPAREN=9;
    public static final int DOT=13;
    public static final int COMMENT=6;
    public static final int ID=7;
    public static final int EOF=-1;
    public static final int STRING=10;

    // delegates
    // delegators

    public Javalex() {;} 
    public Javalex(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public Javalex(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "/home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g"; }

    // $ANTLR start "RULE"
    public final void mRULE() throws RecognitionException {
        try {
            int _type = RULE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:8:5: ()
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:8:7: 
            {
            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:10:5: ( ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' ) )
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:10:7: ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )
            {
            if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||(input.LA(1)>='\f' && input.LA(1)<='\r')||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:13:5: ( '/*' ( . )* '*/' )
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:13:7: '/*' ( . )* '*/'
            {
            match("/*"); 

            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:13:12: ( . )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='*') ) {
                    int LA1_1 = input.LA(2);

                    if ( (LA1_1=='/') ) {
                        alt1=2;
                    }
                    else if ( ((LA1_1>='\u0000' && LA1_1<='.')||(LA1_1>='0' && LA1_1<='\uFFFF')) ) {
                        alt1=1;
                    }


                }
                else if ( ((LA1_0>='\u0000' && LA1_0<=')')||(LA1_0>='+' && LA1_0<='\uFFFF')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:13:12: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            match("*/"); 

            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMENT"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:16:4: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:16:6: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:16:30: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='0' && LA2_0<='9')||(LA2_0>='A' && LA2_0<='Z')||LA2_0=='_'||(LA2_0>='a' && LA2_0<='z')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "OPAREN"
    public final void mOPAREN() throws RecognitionException {
        try {
            int _type = OPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:18:7: ( '(' )
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:18:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OPAREN"

    // $ANTLR start "CPAREN"
    public final void mCPAREN() throws RecognitionException {
        try {
            int _type = CPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:19:7: ( ')' )
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:19:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CPAREN"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:21:7: ( '\"' ( . )* '\"' )
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:21:9: '\"' ( . )* '\"'
            {
            match('\"'); 
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:21:13: ( . )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0=='\"') ) {
                    alt3=2;
                }
                else if ( ((LA3_0>='\u0000' && LA3_0<='!')||(LA3_0>='#' && LA3_0<='\uFFFF')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:21:13: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "INTEGER"
    public final void mINTEGER() throws RecognitionException {
        try {
            int _type = INTEGER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:23:8: ( '0' .. '9' ( '0' .. '9' )* )
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:23:10: '0' .. '9' ( '0' .. '9' )*
            {
            matchRange('0','9'); 
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:23:19: ( '0' .. '9' )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='0' && LA4_0<='9')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:23:20: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INTEGER"

    // $ANTLR start "COMMA"
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:25:6: ( ',' )
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:25:8: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMA"

    // $ANTLR start "DOT"
    public final void mDOT() throws RecognitionException {
        try {
            int _type = DOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:27:4: ( '.' )
            // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:27:6: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOT"

    public void mTokens() throws RecognitionException {
        // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:1:8: ( RULE | WS | COMMENT | ID | OPAREN | CPAREN | STRING | INTEGER | COMMA | DOT )
        int alt5=10;
        alt5 = dfa5.predict(input);
        switch (alt5) {
            case 1 :
                // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:1:10: RULE
                {
                mRULE(); 

                }
                break;
            case 2 :
                // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:1:15: WS
                {
                mWS(); 

                }
                break;
            case 3 :
                // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:1:18: COMMENT
                {
                mCOMMENT(); 

                }
                break;
            case 4 :
                // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:1:26: ID
                {
                mID(); 

                }
                break;
            case 5 :
                // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:1:29: OPAREN
                {
                mOPAREN(); 

                }
                break;
            case 6 :
                // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:1:36: CPAREN
                {
                mCPAREN(); 

                }
                break;
            case 7 :
                // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:1:43: STRING
                {
                mSTRING(); 

                }
                break;
            case 8 :
                // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:1:50: INTEGER
                {
                mINTEGER(); 

                }
                break;
            case 9 :
                // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:1:58: COMMA
                {
                mCOMMA(); 

                }
                break;
            case 10 :
                // /home/pab/Work/jmxmonitor/xymon_jmx/src/com/pab/jmxmonitor/Javalex.g:1:64: DOT
                {
                mDOT(); 

                }
                break;

        }

    }


    protected DFA5 dfa5 = new DFA5(this);
    static final String DFA5_eotS =
        "\1\1\12\uffff";
    static final String DFA5_eofS =
        "\13\uffff";
    static final String DFA5_minS =
        "\1\11\12\uffff";
    static final String DFA5_maxS =
        "\1\172\12\uffff";
    static final String DFA5_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12";
    static final String DFA5_specialS =
        "\13\uffff}>";
    static final String[] DFA5_transitionS = {
            "\2\2\1\uffff\2\2\22\uffff\1\2\1\uffff\1\7\5\uffff\1\5\1\6\2"+
            "\uffff\1\11\1\uffff\1\12\1\3\12\10\7\uffff\32\4\4\uffff\1\4"+
            "\1\uffff\32\4",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA5_eot = DFA.unpackEncodedString(DFA5_eotS);
    static final short[] DFA5_eof = DFA.unpackEncodedString(DFA5_eofS);
    static final char[] DFA5_min = DFA.unpackEncodedStringToUnsignedChars(DFA5_minS);
    static final char[] DFA5_max = DFA.unpackEncodedStringToUnsignedChars(DFA5_maxS);
    static final short[] DFA5_accept = DFA.unpackEncodedString(DFA5_acceptS);
    static final short[] DFA5_special = DFA.unpackEncodedString(DFA5_specialS);
    static final short[][] DFA5_transition;

    static {
        int numStates = DFA5_transitionS.length;
        DFA5_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA5_transition[i] = DFA.unpackEncodedString(DFA5_transitionS[i]);
        }
    }

    class DFA5 extends DFA {

        public DFA5(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 5;
            this.eot = DFA5_eot;
            this.eof = DFA5_eof;
            this.min = DFA5_min;
            this.max = DFA5_max;
            this.accept = DFA5_accept;
            this.special = DFA5_special;
            this.transition = DFA5_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( RULE | WS | COMMENT | ID | OPAREN | CPAREN | STRING | INTEGER | COMMA | DOT );";
        }
    }
 

}