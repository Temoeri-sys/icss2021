grammar ICSS;

//--- LEXER: ---

// IF support:
IF: 'if';
ELSE: 'else';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';

//Literals
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;

//Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

//Specific identifiers for id's and css classes
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

//General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

//All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

// Basic elements
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';


// Copyright to Temoeri Sjamojani

// Requirement "Beperkingen". Please see ASSIGNMENT.md. Limit the property selection to these options.
COLOR_PROP: 'color';
BGC_PROP: 'background-color';
WIDTH_PROP: 'width';
HEIGHT_PROP: 'height';

//--- PARSER: ---
stylesheet: statement*;

// Available statements. Seperated so we can use * next to the variable.
statement
    : selectorStatement
    | variableStatement
    | expressionStatement
    ;

//// ========== Selectors ==========

// Support if[var]
expressionStatement: baseIfStructure expressionProperties ELSE expressionProperties;

// Support <option> { key: val; }
selectorStatement: selectorAssignmentOptions OPEN_BRACE properties* CLOSE_BRACE;

// Syntax to support MyVar := TRUE;
variableStatement: variableAssimentOption variableProperties SEMICOLON;

//// ========== Property structures  ==========

expressionProperties: properties | baseIfStructure;


// TODO: Find out why properties don't work when you use specific prefix like background-color: x;
// Support the following elements to be set as a property
properties: colorSchemeOptions | ratioOptions;

// Variable options support both custom options and base options.
variableProperties: baseCustomVariableOptions | baseColorSchemeOptions | baseRatioOptions | operatorCalculationRules;

// Supports calculation with variable elements
operatorCalculationRules
    : percentageCalculateionRule
    | pixelCalculateionRule
    | variableCalculateionRule
    | allowMultiplyPixelCalculationRule
    | allowMultiplyPercentageCalculationRule
    | allowMultiplyVariableCalculationRule
    ;

//// ========== Options ==========
selectorAssignmentOptions: ID_IDENT | CLASS_IDENT | LOWER_IDENT;

variableAssimentOption: CAPITAL_IDENT ASSIGNMENT_OPERATOR;

// TODO: Figure out why colorSchemeOptions & ratioOptions don't work with _PROP lexer elements but do work with LOWER_IDENT.
// Color/Background-color support structure
colorSchemeOptions: (BGC_PROP | COLOR_PROP) COLON (baseColorSchemeOptions | CAPITAL_IDENT) SEMICOLON;

// Width/height support to accept pixel sizes or percentages only.
ratioOptions: (HEIGHT_PROP | WIDTH_PROP) COLON (baseRatioOptions | CAPITAL_IDENT | operatorCalculationRules) SEMICOLON;

// Variable options have more but since they are covered by the baseOptions, we only add the "different" values.
baseCustomVariableOptions: TRUE | FALSE;

// If we support more color schemes add here (example rgb etc)
baseColorSchemeOptions: COLOR;

// if we support more width/height options add them here.
baseRatioOptions: (PIXELSIZE | PERCENTAGE);

// Support following structure: if[MyVar]
baseIfStructure: IF BOX_BRACKET_OPEN CAPITAL_IDENT BOX_BRACKET_CLOSE;

//// ========== Requirement CH02 & https://github.com/michelportier/icss2021/blob/master/ASSIGNMENT.md#berekende-waardes ==========

// Accepts (N)% +/- (N)%
percentageCalculateionRule: PERCENTAGE baseEqualTypeOperator PERCENTAGE;
// Accepts (n)px +/- (n)px;
pixelCalculateionRule: PIXELSIZE baseEqualTypeOperator PIXELSIZE;
// Accepts var +/- var;
variableCalculateionRule: CAPITAL_IDENT baseEqualTypeOperator CAPITAL_IDENT;

// Accepts (n)px * (n) OR (n) * (n)px.
allowMultiplyPixelCalculationRule: PIXELSIZE MUL SCALAR  | SCALAR MUL PIXELSIZE;
// Accepts (n)% * (n) OR (n) * (n)%.
allowMultiplyPercentageCalculationRule: PERCENTAGE MUL SCALAR  | SCALAR MUL PERCENTAGE;
// Allowe var * (n) OR (n) * VAR
allowMultiplyVariableCalculationRule:  CAPITAL_IDENT MUL SCALAR | SCALAR MUL CAPITAL_IDENT;

// Simple collection of operator which need both sides to contain the same type.
baseEqualTypeOperator: PLUS | MIN ;