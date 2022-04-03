<div id="top"></div>

<!-- PROJECT SHIELDS -->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/polimi-cmls-22/group7-HW-SC-Radical_Geeks">
    <img src="logo.png" alt="Logo" width="640" height="120">
  </a>

<h3 align="center">FM Synth</h3>

  <p align="center">
   Instrument based on FM synthesis
    <br />
    <a href="https://github.com/polimi-cmls-22/group7-HW-SC-Radical_Geeks"><strong>Explore the docs»</strong></a>
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

### Prerequisites
You have to download Supercollider.
### Installation

1. Download for free Supercollider at [https://supercollider.github.io]
2. Install Supercollider
3. Run the sclang script
4. Experiment with the synth and create awesome sounds!
   

<p align="right">(<a href="#top">back to top</a>)</p>

 

<!-- USAGE EXAMPLES -->
## Usage

<img src="screenshot.png" alt="Logo" width="700" height="350">

The user can congifure fm parameters thanks to some buttons and knobs.
The user can play from an external MIDI device or play our demo.
The user can load or save presets from CSV formatted files.

The first four rows of the CSV file will control respectively the knobs of the four modulators in the following way:
1.	the first value of the i-th row sets the ratio of the i-th modulator (values must be in the [0.1, 20] range with step=0.5). 
2.	the second value of the i-th row sets the attack of the i-th modulator (values must be in the [0.001, 2] range with step=0.01). 
3.	the third value of the i-th row sets the release of the i-th modulator (values must be in the [0.1, 10] range with step=0.01). 
4.	the last value of the i-th row sets the index of the i-th modulator (values must be in the [0.1, 20] range with step=1). 
		
The fifth row will control the knobs of the carrier in the following way:
1.	the first value sets the panning of the master output (values must be in the [-1, 1] range with step=0.1). 
2.	the second value sets the master volume (values must be in the [0.001, 2] range with step=0.01).
3.	the third value sets the master attack (values must be in the [0.001, 2] range with step=0.01). 
4.	the last value sets the master release (values must be in the [0, 1] range). 

The sixth row will control the algorithm of the FMSynth:
1.	the first value (can be chosen between 0 and 1) determine whether the left path (modulators 1 and 3) will be active ('1') or not ('0').
2.	the second value (can be chosen between 0 and 1) determine whether the right path (modulators 2 and 4) will be active ('1') or not ('0').
3.	the third value (can be chosen between 0, 1 and 2) determine whether the modulator 3 is not active ('0') or its input comes from modulator 1 ('1') or from the carrier ('2').
4.	the fourth value (can be chosen between 0, 1 and 2) determine whether the modulator 4 is not active ('0') or its input comes from modulator 2 ('1') or from the carrier ('2')	

The only value of the last row will be used to select (between sine ('0'), triangle ('1') and sawtooth ('2')) the waveshape of the carrier.

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- ROADMAP -->
## Roadmap

- [✓] customizable FM synth
- [✓] you can save and load patterns
- [✓] you can the FM synth as an instrument with a MIDI external device
- [✓] you can play our demo
- [✓] you can choose between a sinusoidal wave, a square wave or a sawtooth wave as carrier
- [✓] you can choose the synthesis parameters (ratio and index)
- [✓] you can setup the envelope (attack and release)
- [✓] you can set the panning
- [✓] you can view the time and frequency scope directly in the main window

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- CONTACT -->
## Contact

Gerardo Cicalese - (gerardo.cicalese@mail.polimi.it) </p>
Alberto Bollino - (alberto.bollino@mail.polimi.it) </p>
Umberto Derme - (umberto.derme@mail.polimi.it) </p>
Giorgio Granello - (giorgio.granello@mail.polimi.it) </p>

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
