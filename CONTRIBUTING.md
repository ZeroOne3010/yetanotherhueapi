# Contributing

Contributions are very welcome! If your contribution is trivial, feel free to submit a pull request directly.
If it's not trivial, please open an issue first to discuss the matter. 

Be nice and assume good faith from other contributors.

## Code conventions

### Formatting

The repository has a [.editorconfig](.editorconfig) file that defines the basic coding conventions, 
so please use an editor that supports it. You may find a list of such editors from https://editorconfig.org/,
but if you use a recent version of any of the top IDEs, you should be good to go. The conventions
that are not defined in the .editorconfig file are based on IntelliJ Idea's default formatting settings.
Just remember to format the code if your IDE does not do it automatically when you save a file.

### Testing

Please write tests for your change. Also run all the existing tests before and after your changes.

### Miscellaneous

* Use descriptive variable names. Bad variables: "x", "supplier", "gType". Good variables: "lightId", 
"apiKeySupplier", "groupType".
* Finalize those variables whose value never changes
* Don't do empty `catch` blocks
* Use the `@Override` annotation always when overriding a method of an interface or a superclass
* Do not use wildcard imports (as in `import java.util.*`), always import all the required classes individually

## Commit conventions

* Use descriptive commit messages. Use present tense. Capitalize the first letter. 
  * Examples of good commit messages: "Add support for zones" or "Refactor: extract method". 
  * Bad commit messages: "Fixes", "Added support for zones", "refactor: extract method"
  * If there's an issue about your change, it would be a great idea to add the issue number into your commit message,
  as in "Add support for xyz (#123)" or even "Add support for xyz. Closes #123." if your commit resolves the issue for good.
  * Further reading:
    * [Egwueny Gift: Useful Tips for writing better Git commit messages](https://code.likeagirl.io/useful-tips-for-writing-better-git-commit-messages-808770609503)
    * [Chris Beams: How to Write a Git Commit Message](https://chris.beams.io/posts/git-commit/)
* Micro-commits for the win
  * If you need to use the word "and" in you commit message, you are probably trying to do too much at once:
    * Examples of bad commits: "Remove an unused logger and fix typos", "Add new feature and refactor".
    Split those in two separate commits.
  * Do not split the commit into too small chunks either. If implementing a new feature requires adding
  three and modifying five files, so be it. The important thing to remember is that a single commit should
  only do a single logical thing. First add a feature, then refactor in another commit, if needed.
  This makes it much easier to find, say, whether a new issue was introduced in the initial implementation
  of a new feature or in the refactor phase.
  * If you push a commit to your local fork of the repository and find that there's something wrong with it
  or the pull request review brings up some trivial points to improve upon,
  use [amend](https://git-scm.com/docs/git-commit#Documentation/git-commit.txt---amend) or
  [squash](https://stackoverflow.com/questions/5189560/squash-my-last-x-commits-together-using-git)
  to change your initial commit and then force-push the fixed version of the fork instead.
  * Further reading: 
    * [Luke Metcalfe: 5 Reasons for Keeping Your Git Commits as Small as You Can](https://crealytics.com/blog/5-reasons-keeping-git-commits-small/)
    * [Lucas Rocha: Micro Commits](https://lucasr.org/2011/01/29/micro-commits/)
