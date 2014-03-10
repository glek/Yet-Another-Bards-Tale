Contribution Guidelines
=======================

General
-------

* Avoid arbitrary numbering schemes except for external milestones; use commit
  hash names where possible.
    * Exception: class versions, unfortunately
* No deprecation; just delete it.
* Examples if others can't understand whatever it is you are describing.
* '\n' line breaks.
* Assume JDK 7+ (so yes, do use strings in switch/case).
    * At least the TA uses the latest JRE.

Version Control
---------------

* Branch names match the `githubUserName.*` regex.
* Don't merge into master unless it's stable.
* Don't merge directly into master; create a merge branch or merge into your
  own before merging into master. Master only to be used for 'releases'.

Java Code
---------

### General ###
* Soft line length limit of 80 characters, hard limit of 120 characters.
* 4-space tabstops, expand tabs (ergo, use spaces rather than tabs).
* All closing braces at the same indentation as the structure they are
  associated with, not the same indentation as the statements of said
  structure.
* Don't return NULL if you can use exceptions.
* If you create an overridden method that does nothing, unless the rest of the
  code won't compile, comment it out. Otherwise, one ends up with pollution in
  the javadoc and potentially misleading documentation.

### Constants ###
* Common prefix for related constants (ex: `COLOUR_`).

### Symbol names ###
* Variables in camelCase.
* Classes in PascalCase.
* Constants in UPPER_CASE_UNDERSCORED

### Control Structures ###
* Single-statement control structures without braces.
* Opening brace on the same line as the last line of the control structure
  itself.

    foo (bar) {
        baz;
        qux;
    }

    foo (bar)
        baz;

* No one-liner control structures (ex: `foo (bar): baz;`) unless readability is
  absolutely hampered otherwise.
* Executable statements within conditionals if it eliminates flag variables.
* 'Compound` statements (if-else, try-catch) as follows.

    foo (bar) {
    } baz (qux) {
    } quuux {
    }

* If-else statements where at least one clause has more than a single statement
  long (ergo, uses a `{}` block) must use braces in all blocks.

### Comments ###
* Specific notes requiring action must either use `XXX:`, `FIXME:`, or `TODO:`
  as you feel appropriate.
* Multi-line comments using multi-line comments (ergo: no `//` accross many
  lines unless they are separate comments).

### Methods ###
* Opening brace on the same line as the method signature.
* Avoid really long method signatures.
* Method signatures that are longer than 80 characters are broken across lines
  according to the following template.

        public foo bar(int sgisfsdfhsdfs,
                       String agiughadsifohisdhfadf,
                       Spam ufhsfsudfhuihfasdasd);

* Compiler directives (ex: `@Override`) on line preceding the method signature.
* Use COMMA SPACE to separate each argument.

Documentation
-------------

* Abridged quotation from ESR's "The Art of UNIX Programming", Chapter 19:

    > README
    > The roadmap file, to be read first.
    > 
    > INSTALL
    > Configuration, build, and installation instructions.
    > 
    > AUTHORS
    > List of project contributors (GNU convention).
    > 
    > LICENSE
    > Project license terms.

* `@see` for all tightly coupled code.
* Plain Markdown or Pandoc Markdown for plain text documentation.
* Javadoc tags ordered as per the [Oracle
  convention](http://www.oracle.com/technetwork/java/javase/documentation/index-137868.html#orderoftags).
* Per-class, minimum of:
    * `@author`
    * `@date`, *KEPT UP TO DATE*
    * `@version: branchName targetMilestoneNumber.yourChoiceOfNumberingScheme`
    * 1-liner class description
    * More detailed class description unless the class is dead-simple
* Per-method, minimum of:
    * 1-liner description of method
    * Longer description if necessary
    * `@throws` if any exceptions
    * `@param` if any
    * `@return` if not specified by the 1-liner description
    * `@exception` if any
* Do not rewrite javadoc for overwritten methods unless the semantics are
  different in such a way that they need additional documentation. Modern
  javadoc generators will just copy the description from the parent.

Directory Structure
-------------------

* No Word or Libreoffice documents; everyting in plain text.
* Keep the top level directory clean.
* Tool-specific files kept outside of the repository.

- src
  `- *.java
- bin
  `- *.class
- doc
  `- javadoc
  `- uml
    `- *.png
    `- *.violet
