/*
 1. YES
 2. YES
 3. YES
 4. NO
 5. YES
 6. -- Runs out of memory
 7. YES
 8. NO
 9. NO
 10. YES
*/

?-op(140, fy, neg).
?-op(160, xfy, [and, or, imp, revimp, uparrow, downarrow, notimp, notrevimp, equiv, notequiv]).

/** member(Item, List) :- Item occurs in List
*/

member(X, [X | _]).
member(X, [_ | Tail]) :- member(X, Tail).

/** remove(Item, List, Newlist) :- Newlist is the result of removing all occurrences of Item from List.
*/

remove(_, [], []).
remove(X, [X | Tail], Newtail) :- remove(X, Tail, Newtail).
remove(X, [Head | Tail], [Head | Newtail]) :- not(X=Head), remove(X, Tail, Newtail).

/**sortAll(Old, New) :- Sorts all the lists in a list of list, sort removes duplicates in Prolog
*/

sortAll([], []).
sortAll([Head | Rest], [New | NewRest]) :-
    sort(Head, New),
    sortAll(Rest, NewRest).


/**sortPrime(Old, New) :- uses SortAll and then removes any list duplicates
*/
sortPrime([], []).
sortPrime(ListOfLists, CompleteSort) :-
    sortAll(ListOfLists, SubListsSorted),
    sort(SubListsSorted, CompleteSort).

/** conjunctive(X) :- X is an alpha formula.
*/
conjunctive(_ and _).
conjunctive(neg(_ or _)).
conjunctive(neg(_ imp _)).
conjunctive(neg(_ revimp _)).
conjunctive(neg(_ uparrow _)).
conjunctive(_ downarrow _).
conjunctive(_ notimp _).
conjunctive(_ notrevimp _).

/** disjunctive(X) :- X is a beta formula.
*/

disjunctive(neg(_ and _)).
disjunctive(_ or _).
disjunctive(_ imp _).
disjunctive(_ revimp _).
disjunctive(_ uparrow _).
disjunctive(neg(_ downarrow _)).
disjunctive(neg(_ notimp _)).
disjunctive(neg(_ notrevimp _)).

/** secondary(X) :- X is a equivalence or negated equivalence.
*/

secondary(_ equiv _).
secondary(_ notequiv _).
secondary(neg(_ equiv _)).
secondary(neg(_ notequiv _)).


/** unary(X) :- X is a double negation or a negated constant.
*/

unary(neg neg _).
unary(neg true).
unary(neg false).

/* complement(Old, New) :- Gives the simplfied negation of Old, avoid double negation
*/
complement(X, neg X).
complement(neg X, X).

/** components(X, Y, Z) :- Y and Z are the components of the formula X, as defined in the alpha and beta table.
*/

components(X and Y, X, Y).
components(neg(X and Y), neg X, neg Y).
components(X or Y, X, Y).
components(neg(X or Y), neg X, neg Y).
components(X imp Y, neg X, Y).
components(neg(X imp Y), X, neg Y).
components(X revimp Y, X, neg Y).
components(neg(X revimp Y), neg X, Y).
components(X uparrow Y, neg X, neg Y).
components(neg(X uparrow Y), X, Y).
components(X downarrow Y, neg X, neg Y).
components(neg(X downarrow Y), X, Y).
components(X notimp Y, X, neg Y).
components(neg(X notimp Y), neg X, Y).
components(X notrevimp Y, neg X, Y).
components(neg(X notrevimp Y), X, neg Y).

/** components(X, Y) :- Y is the component of the unary formula X.
*/

component(neg neg X, X).
component(neg true, false).
component(neg false, true).

/** components(X,Y,Z,A,B) :- Y through B are the components of X, designed for equiv and notequiv
and the negated variants
*/

components(X equiv Y, neg X, Y, X, neg Y).
components(X notequiv Y, neg X, neg Y, X, Y).
components(neg(X equiv Y), neg X, neg Y, X, Y).
components(neg(X notequiv Y), neg X, Y, X, neg Y).

/** singleStep(Old, New) :- New is the result of applying a single step of the expansion to Old
*/

singleStep([Disjunction | Rest], New) :-
    member(Formula, Disjunction),
    unary(Formula),
    component(Formula, Newformula),
    remove(Formula, Disjunction, Temporary),
    Newdisjunction = [Newformula | Temporary],
    New = [Newdisjunction | Rest].

singleStep([Disjunction | Rest], New) :-
    member(Beta, Disjunction),
    disjunctive(Beta),
    components(Beta, Betaone, Betatwo),
    remove(Beta, Disjunction, Temporary),
    Newdis = [Betaone, Betatwo | Temporary],
    New = [Newdis | Rest].

singleStep([Disjunction | Rest], New) :-
    member(Alpha, Disjunction),
    conjunctive(Alpha),
    components(Alpha, Alphaone, Alphatwo),
    remove(Alpha, Disjunction, Temporary),
    Newdisone = [Alphaone | Temporary],
    Newdistwo = [Alphatwo | Temporary],
    New = [Newdisone, Newdistwo | Rest].

singleStep([Disjunction | Rest], New) :-
    member(Secondary, Disjunction),
    secondary(Secondary),
    components(Secondary, X1, X2, X3, X4),
    remove(Secondary, Disjunction, Temporary),
    Newdisone = [X1, X2 | Temporary],
    Newdistwo = [X3, X4 | Temporary],
    New = [Newdisone, Newdistwo | Rest].

singleStep([Disjunction | Rest], [Disjunction | Newrest]) :-
    singleStep(Rest, Newrest).

/** expand(Old, New) :- New is the CNF version of Old.
*/
expand(Form, Cnf) :-
    singleStep(Form, Temp),
    expand(Temp, Cnf).

expand(Form, Form).

/** cnf(Old, New) :- Takes a simple propositional formula then outputs in CNF, handles the brackets i.e. [[X]]
*/
cnf(X, Y) :- expand([[X]], Y).


/** expandAll(Old, New) :- Old is a list of propositional formulae, New is the same list but in CNF.
*/
expandAll([], []).
expandAll([Head | Rest], [New | NewRest]) :-
    expand([[Head]], New),
    expandAll(Rest, NewRest).

/* trivialRes(Old, New) :- Gets rid of all trivial resolutions in Old to make New
i.e Occurences of Top or equivalent in a disjuremove the disjunction from Old
*/
trivialRes([Disjunction | Rest], NewRest) :-
    member(P, Disjunction),
    complement(P, NotP),
    member(NotP, Disjunction),
    trivialRes(Rest, NewRest).

trivialRes([Disjunction | Rest], NewRest) :-
    member(true, Disjunction),
    trivialRes(Rest, NewRest).

trivialRes([Disjunction | Rest], [Disjunction| NewRest]) :-
    trivialRes(Rest, NewRest).

trivialRes([], []).

/* resolve(X, Y, Result) - Takes two clauses and tries to find complementary literals and merges the remaining literals into Result.
If complimentary literals aren't found output "notPossible"
*/
resolve(X, Y, Result) :-
    member(L1, X),                      
    member(L2, Y),                      
    complement(L1, L2),                 
    remove(L1, X, Rest1),               
    remove(L2, Y, Rest2),               
    append(Rest1, Rest2, Rest),         
    sort(Rest, Result).

resolve(_, _, notPossible).

/* resolveWithList(Clause, List, New) :- Resolves on all possibilities between Clause and elements in List, output in New 
*/
resolveWithList(_, [], []).
resolveWithList(Clause, [Other|Rest], [Resolution|Resolutions]) :-
    resolve(Clause, Other, Resolution), % Apply resolution on this pair of clauses,
    resolveWithList(Clause, Rest, Resolutions).

/** resolveOnClause(Clause, List, Accumulator, New) :- Aims to resolve Clause with all elements in List and the resolutions from List, and those resolution and so on
Accumulator holds all resolutions, used to ensure tail recursion
*/
resolveOnClause(_, [], FinalAcc, FinalAcc).
resolveOnClause(Clause, List, Acc, Result) :-
    resolveWithList(Clause, List, Resols),                           %Find the possible resolutions on given Clause and initial list clauses
    remove(notPossible, Resols, CleanResols),                        %remove "notPossible" in the Resolutions
    trivialRes(CleanResols, CleanResols2),                           % Remove trivial resolutions
    sortPrime(CleanResols2, CleanResols3),                           % Remove duplicate clauses
    append(CleanResols3, Acc, NewAcc),                              % Add new resolutions to accumulator
    resolveOnClause(Clause, CleanResols3, NewAcc, Result).           % Continue with rest 

/** resolveClauses(List, Accumulator, New) Aims to find all resolutions that can be obtained from one resolution per disjunction pair.
    Accumulator hold every element that started initially and was generated to ensure tail recursion.
*/
resolveClauses([], FinalAcc, FinalAcc).
resolveClauses([Clause|Rest], Acc, New) :-
    resolveOnClause(Clause, Rest, [], Resolutions),        %Find all possible resolution (including recursive ones) between head clause and tail 
    append(Rest, Resolutions, NewRest),                             %Ensure to add the resolution from above to the tail of the list
    sortPrime(NewRest, NewRest2),
    resolveClauses(NewRest2, [Clause|Acc], New).                   %Recursively find all resolutions for clauses number 2,3,4,5 and so on

/** applyResolution(Old, New) :- Takes a CNF formula and applies as much resolution as possible. New is a list of the resolutions as that is where a [] will occur
*/
apply_resolution(Clauses, New) :-
    resolveClauses(Clauses, [], New).

/**closed(List) Looks for an empty list in List, i.e. empty disjunction, which would show a contradiction
*/
closed([[] | _]) :- true.
closed([_ | Rest]) :- closed(Rest).
closed([]) :- false.


/** ifElse(P, Q, R) :- emulate an if else construction.
*/

ifElse(P, Q, _) :- P, !, Q.
ifElse(_, _, R) :- R.

/** yes, no :- functions to write to standard output when we find or don't find a contradiction
*/
yes :- write('YES'), nl. 
no :- write('NO'), nl.

/** test(Premises, Formula) :- Takes in the formulae, creates a big CNF formula, applies resolution and outputs Formula is a consequence of Premises
*/
test(Premises, X) :- 
    expand([[neg X]], Y),
    expandAll(Premises, CnfPremises),
    append(CnfPremises, BigCnf),
    append(BigCnf, Y, CompleteCnf),
    sortPrime(CompleteCnf, Sorted),
    trivialRes(Sorted, Temp),
    apply_resolution(Temp ,Temp2),
    ifElse(closed(Temp2), yes, no).
