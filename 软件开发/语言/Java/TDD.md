# TDD

基于代码的某个单元，使用Mock等技术编写单元测试，然后用这个单元测试来驱动开发，抑或是帮助在重构、修改以后进行回归测试。而现在大部分反对TDD的声音就是基于这个理解，比如：

- 工期紧，时间短，写TDD太浪费时间；
- 业务需求变化太快，修改功能都来不及，根本没有时间来写TDD；
- 写TDD对开发人员的素质要求非常高，普通的开发人员不会写；
- TDD 推行的最大问题在于大多数程序员还不会「写测试用例」和「重构」；
- 由于大量使用Mock和Stub技术，导致UT没有办法测试集成后的功能，对于测试业务价值作用不大

技术人员拒绝TDD的主要原因在于难度大、工作量大、Mock的大量使用导致很难测试业务价值等。
这里的测试并不只是单元测试，也不是说一定要使用mock和stub来做测试。这里的测试就是指软件测试本身，可以是基于代码单元的单元测试，可以是基于业务需求的功能测试，也可以是基于特定验收条件的验收测试
让TDD成为自己工作习惯的一部分，这样才能更好的提升软件质量，大大降低软件维护成本。不管你信不信，反正我信了