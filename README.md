<div id="top"></div>
<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->



<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]





<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/polimi-cmls-22/group7-HW-SC-Radical_Geeks">
    <img src="logo.png" alt="Logo" width="640" height="120">
  </a>

<h3 align="center">FM Synth</h3>

  <p align="center">
   Instrument based on FM synthesis.
    <br />
    <a href="https://github.com/polimi-cmls-22/group7-HW-SC-Radical_Geeks"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/polimi-cmls-22/group7-HW-SC-Radical_Geeks">View Demo</a>
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contact">Contact</a></li>

  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

<p align="right">(<a href="#top">back to top</a>)</p>

### Built With

* [Supercollider](https://supercollider.github.io/)


<p align="right">(<a href="#top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

This is an example of how you may give instructions on setting up your project locally.
To get a local copy up and running follow these simple example steps.

### Prerequisites
You have to download Supercollider.
### Installation

1. Get for free Supercollider at [https://supercollider.github.io];
2. Install Supercollider;  
3. Run the script in sclang;
4. Experiment with the synth and create your own sound;
   

<p align="right">(<a href="#top">back to top</a>)</p>

 

<!-- USAGE EXAMPLES -->
## Usage

The user has to set parameters and push buttons on the right to activate modulation which characterize FM Synth.
The user can play from a MIDI external device or implement a sequencing technique with supercollider.
The user can load or save presets using CSV files that will be read or written in SC thanks to the actions of the buttons 'LOAD' and 'SAVE'. The CSV file will always have the same layout of six rows with four elements each and a seventh row with only one value.
The first four rows will control respectively the knobs of the four modulators in the following way:
1.	the first value of the i-th row sets the ratio of the i-th modulator (values must be in the [0.1, 20] range with step=0.5). 
2.	the second value of the i-th row sets the attack of the i-th modulator (values must be in the [0.001, 2] range with step=0.01). 
3.	the third value of the i-th row sets the release of the i-th modulator (values must be in the [0.1, 10] range with step=0.01). 
4.	the last value of the i-th row sets the index of the i-th modulator (values must be in the [0.1, 20] range with step=1). 
		
The fifth row will control the knobs of the master (carrier) in the following way:
1.	the first value sets the panning of the master output (values must be in the [-1, 1] range with step=0.1). 
2.	the second value sets the master volume (values must be in the [0.001, 2] range with step=0.01).
3.	the third value sets the master attack (values must be in the [0.001, 2] range with step=0.01). 
4.	the last value sets the master release (values must be in the [0, 1] range). 

The sixth row will control the topography of the FMSynth:
1.	the first value (can be chosen between 0 and 1) determine whether the left path (modulators 1 and 3) will be active ('1') or not('0').
2.	the second value (can be chosen between 0 and 1) determine whether the right path (modulators 1 and 3) will be active ('1') or not ('0').
3.	the third value (can be chosen between 0, 1 and 2) determine whether the modulator 3 is not active ('0') or its input comes from modulator 1 ('1') or from the carrier('2').
4.	the fourth value (can be chosen between 0, 1 and 2) determine whether the modulator 4 is not active ('0') or its input comes from modulator 2 ('1') or from the carrier('2')	

The only value of the last row will be used to select (between sine ('0'), triangle ('1') and sawtooth ('2')) the waveshape of the carrier.




_For more examples, please refer to the [Documentation](https://example.com)_

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- ROADMAP -->
## Roadmap

- [✓] customizable FM synth, salvataggio e carimento file 
- [✓] you can save and upload from file
- [✓] you can create any sound and combine any effect
- [✓] For playing your instrument you can either use a MIDI external device or implement a sequencing technique with supercollider
- [✓] you can choose a sinusoidal wave, a square wave or a as master oscilation 
- [✓] you can choose the synthesis parameters

See the [open issues](https://github.com/polimi-cmls-22/group7-HW-SC-Radical_Geeks/issues) for a full list of proposed features (and known issues).

<p align="right">(<a href="#top">back to top</a>)</p>




<!-- CONTACT -->
## Contact

Gerardo Cicalese- gerardo.cicalese@mail.polimi.it)
Alberto Bollino- (alberto.bollino@mail.polimi.it)
Umberto Derme- (umberto.derme@mail.polimi.it)
Giorgio Granello- (giorgio.granello@mail.polimi.it)

Project Link: [https://github.com/polimi-cmls-22/group7-HW-SC-Radical_Geeks](https://github.com/polimi-cmls-22/group7-HW-SC-Radical_Geeks)

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/polimi-cmls-22/group7-hw-SC-Radical_Geeks.svg?style=for-the-badge
[contributors-url]: https://github.com/polimi-cmls-22/group7-hw-SC-Radical_Geeks/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/polimi-cmls-22/group7-hw-SC-Radical_Geeks.svg?style=for-the-badge
[forks-url]: https://github.com/polimi-cmls-22/group7-hw-SC-Radical_Geeks/network/members
[stars-shield]: https://img.shields.io/github/stars/polimi-cmls-22/group7-hw-SC-Radical_Geeks.svg?style=for-the-badge
[stars-url]: https://github.com/polimi-cmls-22/repo_name/stargazers
[issues-shield]: https://img.shields.io/github/issues/polimi-cmls-22/group7-hw-SC-Radical_Geeks.svg?style=for-the-badge
[issues-url]: https://github.com/polimi-cmls-22/group7-hw-SC-Radical_Geeks/issues
[license-shield]: https://img.shields.io/github/license/polimi-cmls-22/group7-hw-SC-Radical_Geeks.svg?style=for-the-badge
[license-url]: https://github.com/polimi-cmls-22/group7-hw-SC-Radical_Geeks/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[product-screenshot]: images/screenshot.png
