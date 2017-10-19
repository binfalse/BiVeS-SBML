# BiVeS SBML module

![bives logo](https://sems.uni-rostock.de/wp-content/uploads/2012/12/logo-icon-16.png) BiVeS is a library based on the XyDiff algorithm. It is able to detect differences between two versions of a computational model and to communicate these changes.

The Algorithm implemented in BiVeS was published as
> Martin Scharm, Olaf Wolkenhauer, Dagmar Waltemath:
> [An algorithm to detect and communicate the differences in computational models describing biological systems.](https://doi.org/10.1093/bioinformatics/btv484)
> *Bioinformatics* 32 (4): 563-570;

This module implements special stategies for computational models encoded in [SBML format](http://sbml.org/).
**More documentation on the BiVeS-SBML** can be found and the [GitHub pages of the SEMS project](https://semsproject.github.io/BiVeS-SBML/).

## BiVeS consists of several modules

![bives modules](https://github.com/binfalse/BiVeS/blob/master/art/dependency-graph.png)

BiVeS itself consists of a number of modules:

* [xmlutils](https://github.com/binfalse/xmlutils) is a library for advanced XML handling
* [jCOMODI](https://github.com/binfalse/jCOMODI/) provides programmatic access to the [COMODI ontology](http://purl.uni-rostock.de/comodi/)
* [BiVeS-Core](https://github.com/binfalse/BiVeS-Core) is the core library for comparison of computational models
* [BiVeS-SBML](https://github.com/binfalse/BiVeS-SBML/) is a module providing special strategies for models encoded in SBML
* [BiVeS-CellML](https://github.com/binfalse/BiVeS-CellML) is a module providing special strategies for models encoded in CellML
* [BiVeS](https://github.com/binfalse/BiVeS) ties all modules together and provides command line access
* [BiVeS-WebApp](https://github.com/binfalse/BiVeS-WebApp) is a web interface to access BiVeS through the network
* [BiVeS-WebApp-Client](https://github.com/binfalse/BiVeS-WebApp-Client) provides a Java library for comparing models using the BiVeS-WebApp


## Usage of BiVeS

To use BiVeS you should have a look at the [BiVeS framework](https://github.com/binfalse/BiVeS) or the [BiVeS-WebApp](https://github.com/binfalse/BiVeS-WebApp).

## LICENSE

Artwork and text etc is licensed under a [Creative Commons Attribution-ShareAlike 4.0 International License](http://creativecommons.org/licenses/by-sa/4.0/) ![Creative Commons License](https://i.creativecommons.org/l/by-sa/4.0/80x15.png)

The code is licensed under an [Apache 2.0 license](LICENSE):

    Copyright martin scharm <https://binfalse.de/contact/>

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

