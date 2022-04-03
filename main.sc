/*
Gruppo: Radical Geeks

Componenti:
Gerardo Cicalese
Alberto Bollino
Giorgio Granello
Umberto Derme

Consegna numero 2 --> FM SYNTHESIZER
*/

s.waitForBoot({
	//here we can find the definition of all the variables used in the following code
	var freq, ratios, indexes, atks, rels, mod, car, bus, master, pan, w, ratioKnob, attackKnob, releaseKnob, amplitudeKnob, indexKnob, panningKnob, masterKnob, btnPlay, amps, masterRatioKnob, masterAttackKnob, masterReleaseKnob, masterAttack, masterRelease, masterAmplitudeKnob, masterIndexKnob, masterPanningKnob, g, enabled_13, enabled_24, mode_3, mode_4, b_13, b_24, b_mode3, b_mode4, b_waveSelection, buttons_font, b_scope, b_freqScope, b_demo, b_loadFile, b_Save, stringa, string_tot, carrierType, f2, c2;

	carrierType = 2;

	//create two buffers used for the scope and freqscope windows
	f = Buffer.alloc(s,1024,2);
	f2 = Buffer.alloc(s,1024,2);

	//Definition of the modulator synth
	SynthDef(\modulator, {
		arg busIn, busOut, freq, atk = 0.01, rel = 4, amp = 1;
		var osc, env;

		//envelope
		env = EnvGen.kr(Env.perc(atk, rel), doneAction:2);

		osc = SinOsc.ar(freq + In.ar(busIn));

		//direct out
		Out.ar(busOut, osc*env*amp);
	}).add;

	//Definition of the carrier (master out) synth
	SynthDef(\sinCarrier, {
		arg busIn, freq, atk = 0.01, rel = 4, amp = 1, pan = 0, fbBus = nil, bufnum, bufnum2;
		var osc, env;

		//envelope
		env = EnvGen.kr(Env.perc(atk, rel), doneAction:2);

		osc = SinOsc.ar([freq + In.ar(busIn), freq + In.ar(busIn)]);

		ScopeOut2.ar(osc*env*amp, bufnum);
		ScopeOut2.ar(osc*env*amp, bufnum2);

		//direct out
		Out.ar([0, 1], Pan2.ar(osc*env*amp, pan));
		Out.ar(fbBus, osc*env*amp);
	}).add;

	SynthDef(\triCarrier, {
		arg busIn, freq, atk = 0.01, rel = 4, amp = 1, pan = 0, fbBus = nil, bufnum, bufnum2;
		var osc, env;

		//envelope
		env = EnvGen.kr(Env.perc(atk, rel), doneAction:2);

		osc = LFTri.ar([freq + In.ar(busIn), freq + In.ar(busIn)]);

		ScopeOut2.ar(osc*env*amp, bufnum);
		ScopeOut2.ar(osc*env*amp, bufnum2);

		//direct out
		Out.ar([0, 1], Pan2.ar(osc*env*amp, pan));
		Out.ar(fbBus, osc*env*amp);
	}).add;

	SynthDef(\sawCarrier, {
		arg busIn, freq, atk = 0.01, rel = 4, amp = 1, pan = 0, fbBus = nil, bufnum, bufnum2;
		var osc, env;

		//envelope
		env = EnvGen.kr(Env.perc(atk, rel), doneAction:2);

		osc = LFSaw.ar([freq + In.ar(busIn), freq + In.ar(busIn)]);

		ScopeOut2.ar(osc*env*amp, bufnum);
		ScopeOut2.ar(osc*env*amp, bufnum2);

		//direct out
		Out.ar([0, 1], Pan2.ar(osc*env*amp, pan));
		Out.ar(fbBus, osc*env*amp);
	}).add;

	//variables used to define the FMSynth sound path
	enabled_13 = 0;  //enables the synthesizers 1 and 3
	enabled_24 = 0;  //enables the synthesizers 2 and 4
	mode_3 = 0;      //set the input for the Synth number 3 (0=no input, 1=input from 1, 2=feedback from master)
	mode_4 = 0;      //set the input for the Synth number 4 (0=no input, 1=input from 1, 2=feedback from master)


	//here we have created 3 arrays that are used to contain all the various modulators, the busses used to connect them and all the control-specs (g) used for the knobs.
	mod = Array.newClear(4);
	bus = Array.newClear(3);
	g = Array.newClear(5);

	MIDIClient.init;
	MIDIIn.connectAll;

	//initializing all the values used in the sound definition
	pan = 0;
	freq = 440;
	master = 1;
	ratios = Array.fill(4, {arg i; 1;});
	indexes = Array.fill(4, {arg i; 1;});
	atks = Array.fill(4, {arg i; 1;});
	rels = Array.fill(4, {arg i; 1;});

	//defining the three busses as mono channels
	bus[0] = Bus.audio(s, 1);
	bus[1] = Bus.audio(s, 1);
	bus[2] = Bus.audio(s, 1);

	//GUI*************************************************************************
	GUI.current;
	j = 100;
	n = 4;    //number of modulators + carrier  (array starts from 0, so we get 5 elements when using n)

	ratioKnob = Array.newClear(n);
	releaseKnob = Array.newClear(n);
	attackKnob = Array.newClear(n);
	amplitudeKnob = Array.newClear(n);
	indexKnob = Array.newClear(n);

	//defining all the ControlSpec that we'll use to set the bounds and the steps of all the knobs
	g[0] = ControlSpec.new(0.1, 20, \lin, 0.5); //ratio
	g[1] = ControlSpec.new(0.001, 2, \lin, 0.01); //attack
	g[2] = ControlSpec.new(0.1, 10, \lin, 0.01); //release
	g[3] = ControlSpec.new(-1, 1, \lin, 0.1); //panning
	g[4] = ControlSpec.new(0.1, 20, \lin, 1); //index

	//definition of the window inside which all the other elements (views) will be displayed
	w = Window("FM SYNTH", Rect(100,100,930,420), false);

	//SCscope
	c = ScopeView(w, Rect((10 + ((4+3)*100)),10,210,200));
	c2 = FreqScopeView(w, Rect((10 + ((4+3)*100)),210,210,200));
	c.bufnum=f.bufnum;
	c2.bufnum = f2.bufnum;

	c.server = s;
	c2.server = s;


	//graphic representation of the knobs
	n.do({
		arg i;
		ratioKnob[i] = EZKnob(parent:w, bounds:Rect(10 + (i*j),10,100,100), label:"ratio " + (i+1), initVal:0.5, controlSpec:g[0]);
		ratioKnob[i].setColors(Gradient(Color.red,Color.blue), Color.white, Color.black, Color.white, Color.white, background: Gradient(Color.red,Color.blue));

		attackKnob[i] = EZKnob(parent:w, bounds:Rect(10 + (i*j),110,100,100), label:"attack "+(i+1), initVal:0.5, controlSpec:g[1]);
		attackKnob[i].setColors(Gradient(Color.red,Color.blue), Color.white, Color.black, Color.white, Color.white, background: Gradient(Color.red,Color.blue));

		releaseKnob[i] = EZKnob(parent:w, bounds:Rect(10 + (i*j),210,100,100), label:"release "+(i+1), initVal:0.5, controlSpec:g[2]);
		releaseKnob[i].setColors(Gradient(Color.red,Color.blue), Color.white, Color.black, Color.white, Color.white, background: Gradient(Color.red,Color.blue));

		indexKnob[i] = EZKnob(parent:w, bounds:Rect(10 + (i*j),310,100,100), label:"index "+(i+1), initVal:0.5, controlSpec:g[4]);
		indexKnob[i].setColors(Gradient(Color.red,Color.blue), Color.white, Color.black, Color.white, Color.white, background:
			Gradient(Color.red,Color.blue));
	});

	masterAttackKnob = EZKnob(parent:w, bounds:Rect(10 + (n*j),210,100,100), label:"Master attack ", controlSpec:g[1], initVal:0.5);
	masterAttackKnob.setColors(Gradient(Color.red,Color.blue), Color.white, Color.black, Color.white, Color.white, background: Color.grey);

	masterReleaseKnob = EZKnob(parent:w, bounds:Rect(10 + (n*j),310,100,100), label:"Master release ", controlSpec:g[2], initVal:0.5);
	masterReleaseKnob.setColors(Gradient(Color.red,Color.blue), Color.white, Color.black, Color.white, Color.white, background: Color.grey);

	panningKnob = EZKnob(parent:w, bounds:Rect(10 + (n*j),10,100,100), label:"Panning Master", initVal:0.5, controlSpec:g[3]);
	panningKnob.setColors(Color.grey, Color.white, Color.black, Color.white, Color.white, background: Color.grey);
	masterKnob = EZKnob(parent:w, bounds:Rect(10 + (n*j),110,100,100), label:"Master Volume", initVal:0.5);
	masterKnob.setColors(Color.grey, Color.white, Color.black, Color.white, Color.white, background: Color.grey);


	//graphical definition + actions of the buttons
	buttons_font = Font("Helvetica", 10);

	b_13 = Button(w, Rect(10 + ((n+1)*j),50,100,80))
	.states_([
		["left path inactive", Color.black, Color.red],
		["left path active", Color.black, Color.green],
	])
	.action_({ arg butt;
		enabled_13 = butt.value;    //when the button is pressed we'll have a change in value (selected between 0 and 1) for the enalbes_13 variable that will tell us if the left path is active or not
		w.refresh;          //we use refresh to display the change of the topography in the small display
	})
	.font = buttons_font;

	b_24 = Button(w, Rect(10 + ((n+2)*j),50,100,80))
	.states_([
		["right path inactive", Color.black, Color.red],
		["right path active", Color.black, Color.green],
	])
	.action_({ arg butt;
		enabled_24 = butt.value; //the same thing that we have descibed for the b_13 button happens here
		w.refresh;
	})
	.font = buttons_font;

	b_mode3 = Button(w, Rect(10 + ((n+1)*j),130,100,80))
	.states_([
		["not active", Color.black, Color.red],           //no input in the third modulator
		["input from 1", Color.black, Color.green],       //input from modulator 1
		["input from master", Color.black, Color.blue],   //feedback from master (input from the carrier)
	])
	.action_({ arg butt;
		mode_3 = butt.value;  //when the button is pressed we'll have a change in value (selected between 0,1 and 2) for the mode_4 variable that will tell us the input for the modulator 3
		w.refresh;      //we use refresh to display the change of the topography in the small display
	})
	.font = buttons_font;

	b_mode4 = Button(w, Rect(10 + ((n+2)*j),130,100,80))
	.states_([
		["not active", Color.black, Color.red],           //no input in the fourth modulator
		["input from 1", Color.black, Color.green],       //input from modulator 2
		["input from master", Color.black, Color.blue],   //feedback from master (input from the carrier)
	])
	.action_({ arg butt;
		mode_4 = butt.value;    //the same thing that we have descibed for the b_mode3 button happens here
		w.refresh;
	})
	.font = buttons_font;


	b_waveSelection = Button(w, Rect(10 + ((n+2)*j),10,100,20))
	.states_([
		["sine", Color.black, Color.rand],           //sine carrier wave
		["triangle", Color.black, Color.rand],       //triangle carrier wave
		["sawtooth", Color.black, Color.rand],       //sawtooth carrier wave
	])
	.action_({ arg butt;
		carrierType = butt.value;    //the same thing that we have descibed for the b_mode3 button happens here
		w.refresh;
	})
	.font = buttons_font;

	//b_loadFile is a button used to load from a CSV file some predefined values for all the knobs in out GUI
	b_loadFile = Button(w, Rect(10 + ((n+1)*j),30,100,20))
	.action_({ arg butt;
		(
			//the following three lines of code will bring up a popup window thanks to which the user will be able to select an existing CSV file and save it (in a matrix form) inside the variable a.
			FileDialog({ |path|
				postln("Selected path:" + path);
				a = CSVFileReader.read(path).postcs;

				//the code inside the following round brackets will set the values, taken from a, of the knobs
				(
					(n).do({
						|i|
						ratioKnob[i].value = a[i][0];
						ratios[i] = ratioKnob[i].value;
						indexKnob[i].value = a[i][1];
						indexes[i] = indexKnob[i].value;
						attackKnob[i].value = a[i][2];
						atks[i] = attackKnob[i].value;
						releaseKnob[i].value = a[i][3];
						rels[i] = ratioKnob[i].value;
					});

					panningKnob.value = a[n][0];
						pan = panningKnob.value;
					masterKnob.value = a[n][1];
						master = masterKnob.value;
					masterAttackKnob.value = a[n][2];
						masterAttack = masterAttackKnob.value;
					masterReleaseKnob.value = a[n][3];
						masterRelease = masterReleaseKnob.value;

					b_13.value = a[n+1][0].asInteger;
						enabled_13 = b_13.value;
					b_24.value = a[n+1][1].asInteger;
						enabled_24 = b_24.value;
					enabled_24.postln;
					b_mode3.value = a[n+1][2].asInteger;
						mode_3 = b_mode3.value;
					b_mode4.value = a[n+1][3].asInteger;
						mode_4 = b_mode4.value;

					b_waveSelection.value = a[n+2][0];
					    carrierType = b_waveSelection.value;

					w.refresh;
				);
			}, {
				postln("Dialog was cancelled. Try again.");    //whis string will be displayed in the Post Window if the user closes the pop-up window without selecting anything
			}, stripResult: true);
		);

	})
	.font = buttons_font;

	b_loadFile.string = "LOAD";   //String displayed in the GUI as the button name


	//b_Save is a button that will save the current values of the knobs in a file 'ciao.CSV'
	b_Save = Button(w, Rect(10 + ((n+2)*j),30,100,20))
	.action_({ arg butt;
		(
			stringa = Array.newClear(n+3);  //String of 7 elements

			//do cicle used to put the values of the knobs (as a string) of the i-modulator inside the i-element of the string 'stringa'     (first four columns of knobs of the GUI)
			n.do({
				|i|
				stringa[i] = "" ++ ratioKnob[i].value ++ "," ++ indexKnob[i].value ++ "," ++ attackKnob[i].value ++ "," ++ releaseKnob[i].value ++ "\n";
			});

			//load the values, as a string, of the master (carrier) knobs (5-th column of knobs of the GUI)
			stringa[4] = "" ++ panningKnob.value++ "," ++ masterKnob.value ++ "," + masterAttackKnob.value ++ "," ++ masterReleaseKnob.value ++ "\n";

			stringa[5] = "" ++ b_13.value++ "," ++ b_24.value ++ "," ++ b_mode3.value ++ "," ++ b_mode4.value ++ "\n";

			stringa[6] = "" ++ b_waveSelection.value ++ "\n";


			//we'll now create a string as the sum of all the strings that we have created (one for every modulator + one for the carrier) which will be used to save the values in the CSV file.
			string_tot = stringa[0]++stringa[1]++stringa[2]++stringa[3]++stringa[4]++stringa[5]++stringa[6];
			postln(stringa);

			FileDialog({ |path|
				postln("Selected path:" + path);
				f = File(path, "w");
				f.write(
					string_tot;
				);
				f.close;
			}, {
				postln("Dialog was cancelled. Try again.");    //whis string will be displayed in the Post Window if the user closes the pop-up window without selecting anything
			}, stripResult: true);
	)}
	)
	.font = buttons_font;

	b_Save.string = "SAVE";



	//CONTROLLER for the KNOBS
	//ratios
	n.do({
		|i|
		ratioKnob[i].action_({
			ratios[i] = ratioKnob[i].value;
		});
	}
	);

	//attack
	n.do({
		|i|
		attackKnob[i].action_({
			atks[i] = attackKnob[i].value;
		});
	}
	);

	//release
	n.do({
		|i|
		releaseKnob[i].action_({
			rels[i] = ratioKnob[i].value;
		});
	}
	);

	//index
	n.do({
		|i|
		indexKnob[i].action_({
			indexes[i] = indexKnob[i].value;
		});
	}
	);

	//master panning
	panningKnob.action_({
		pan = panningKnob.value;
	});

	//master Volume
	masterKnob.action_({
		master = masterKnob.value;
	});

	//master attack
	masterAttackKnob.action_({
		masterAttack = masterAttackKnob.value;
	});

	//master release
	masterReleaseKnob.action_({
		masterRelease = masterReleaseKnob.value;
	});

	w.front;


	MIDIdef.noteOn(\test, {
		arg vel, note; note.postln;
		note = note.midicps;

		//enabled_13, enabled_24, enabled_fb1, enabled_fb2

		if(enabled_13 == 1, { //if the left forward chain is enabled

			if(mode_3 == 2,//feedback mode
				{
					mod[2] = Synth.new(\modulator, [\freq, note*ratios[2], \atk, atks[2], \rel, rels[2], \amp, note*ratios[2]*indexes[2], \busIn, bus[0], \busOut, bus[2]]);
				}
			);
			if(mode_3 == 1, //1 as input mode
				{
					mod[0] = Synth.new(\modulator, [\freq, note*ratios[0], \atk, atks[0], \rel, rels[0], \amp, note*ratios[0]*indexes[0], \busIn, nil, \busOut, bus[0]]);
					mod[2] = Synth.after(mod[0], \modulator, [\freq, note*ratios[2], \atk, atks[2], \rel, rels[2], \amp, note*ratios[2]*indexes[2], \busIn, bus[0], \busOut, bus[2]]);
				}
			);
			if(mode_3 == 0, //no input mode
				{
					mod[2] = Synth.new(\modulator, [\freq, note*ratios[2], \atk, atks[2], \rel, rels[2], \amp, note*ratios[2]*indexes[2], \busIn, nil, \busOut, bus[2]]);
				}
			);

			if(carrierType == 0,
				{
					car = Synth.after(mod[2], \sinCarrier, [\freq, note, \atk, masterAttack, \rel, masterRelease, \amp, master, \busIn, bus[2], \pan, pan, \fbBus, bus[0], \bufnum, f.bufnum, \bufnum2, f2.bufnum]);
				}
			);
			if(carrierType == 1,
				{
					car = Synth.after(mod[2], \triCarrier, [\freq, note, \atk, masterAttack, \rel, masterRelease, \amp, master, \busIn, bus[2], \pan, pan, \fbBus, bus[0], \bufnum, f.bufnum, \bufnum2, f2.bufnum]);
				}
			);
			if(carrierType == 2,
				{
					car = Synth.after(mod[2], \sawCarrier, [\freq, note, \atk, masterAttack, \rel, masterRelease, \amp, master, \busIn, bus[2], \pan, pan, \fbBus, bus[0], \bufnum, f.bufnum, \bufnum2, f2.bufnum]);
				}
			);

			if(enabled_24 == 1, { //if also the right forward chain is enabled
				if(mode_4 == 2,
					{
						mod[3] = Synth.new(\modulator, [\freq, note*ratios[3], \atk, atks[3], \rel, rels[3], \amp, note*ratios[3]*indexes[3], \busIn, bus[0], \busOut, bus[2]]);
					}
				);
				if(mode_4 == 1,
					{
						mod[1] = Synth.new(\modulator, [\freq, note*ratios[1], \atk, atks[1], \rel, rels[1], \amp, note*ratios[1]*indexes[1], \busIn, nil, \busOut, bus[1]]);
						mod[3] = Synth.after(mod[1], \modulator, [\freq, note*ratios[3], \atk, atks[3], \rel, rels[3], \amp, note*ratios[3]*indexes[3], \busIn, bus[1], \busOut, bus[2]]);
					}
				);
				if(mode_4 == 0,
					{
						mod[3] = Synth.new(\modulator, [\freq, note*ratios[3], \atk, atks[3], \rel, rels[3], \amp, note*ratios[3]*indexes[3], \busIn, nil, \busOut, bus[2]]);
					}
				);
			}
			);
		}
		);

		if((enabled_24 == 1) && (enabled_13 == 0), { //if only the right forward chain is enabled
			if(mode_4 == 2,
				{
					mod[3] = Synth.new(\modulator, [\freq, note*ratios[3], \atk, atks[3], \rel, rels[3], \amp, note*ratios[3]*indexes[3], \busIn, bus[1], \busOut, bus[2]]);
				}
			);
			if(mode_4 == 1,
				{
					mod[1] = Synth.new(\modulator, [\freq, note*ratios[1], \atk, atks[1], \rel, rels[1], \amp, note*ratios[1]*indexes[1], \busIn, nil, \busOut, bus[1]]);
					mod[3] = Synth.after(mod[1], \modulator, [\freq, note*ratios[3], \atk, atks[3], \rel, rels[3], \amp, note*ratios[3]*indexes[3], \busIn, bus[1], \busOut, bus[2]]);
				}
			);
			if(mode_4 == 0,
				{
					mod[3] = Synth.new(\modulator, [\freq, note*ratios[3], \atk, atks[3], \rel, rels[3], \amp, note*ratios[3]*indexes[3], \busIn, nil, \busOut, bus[2]]);
				}
			);

			if(carrierType == 0,
				{
					car = Synth.after(mod[3], \sinCarrier, [\freq, note, \atk, masterAttack, \rel, masterRelease, \amp, master, \busIn, bus[2], \pan, pan, \fbBus, bus[1], \bufnum, f.bufnum, \bufnum2, f2.bufnum]);
				}
			);
			if(carrierType == 1,
				{
					car = Synth.after(mod[3], \triCarrier, [\freq, note, \atk, masterAttack, \rel, masterRelease, \amp, master, \busIn, bus[2], \pan, pan, \fbBus, bus[1], \bufnum, f.bufnum, \bufnum2, f2.bufnum]);
				}
			);
			if(carrierType == 2,
				{
					car = Synth.after(mod[3], \sawCarrier, [\freq, note, \atk, masterAttack, \rel, masterRelease, \amp, master, \busIn, bus[2], \pan, pan, \fbBus, bus[1], \bufnum, f.bufnum, \bufnum2, f2.bufnum]);
				}
			);
		}
		);

	}
	);

	//DEMO SONG
	~bass = Task({
		var bus1, bus2, bus3, delta, midi, dur, note, mod1, mod2, car;

		bus1 = Bus.audio(s, 1);
		bus2 = Bus.audio(s, 1);
		midi = Pseq([32, 32, 35, 39, 44, 40, 40, 37, 37, 37, 39,     32, 32, 35, 39, 44, 40, 40, 37, 37, 37, 39,     32, 32, 35, 39, 44, 40, 40, 37, 37, 37, 39,     32, 32, 35, 39, 37, 37, 37, 37], inf).asStream;
		dur = Pseq([0.5, 0.5, 0.5, 0.4, 0.4, 0.2, 0.4, 0.4, 0.2, 0.4, 0.4,   0.2, 0.5, 0.5, 0.4, 0.4, 0.2, 0.4, 0.4, 0.2, 0.4, 0.1,    0.5, 0.5, 0.5, 0.4, 0.4, 0.2, 0.4, 0.4, 0.2, 0.4, 0.4,   0.2, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5], inf).asStream;

		while {
			delta = dur.next;
			delta.notNil
		} {
			(
				note = midi.next.midicps;

				mod1 = Synth.new(\modulator, [\freq, note*2, \atk, 0, \rel, 0.4, \amp, note*2*5, \busIn, nil, \busOut, bus1]);
				mod2 = Synth.after(mod1, \modulator, [\freq, note, \atk, 0.03, \rel, 0.4, \amp, note*1*4.5, \busIn, bus1, \busOut, bus2]); //note*1*9/2

				car = Synth.after(mod2, \sinCarrier, [\freq, note, \atk, 0.05, \rel, 0.4, \amp, master*0.1, \busIn, bus2, \pan, pan, \bufnum, f.bufnum, \bufnum2, f2.bufnum]);

				delta.yield;
			)
		}
	});

	~kick = Task({
		var bus1, bus2, bus3, delta, midi, dur, note, mod1, mod2, car;

		bus1 = Bus.audio(s, 1);
		bus2 = Bus.audio(s, 1);

		delta = 0.5;

		while {
			0 == 0;
		} {
			(
				note = 31.midicps;

				mod1 = Synth.new(\modulator, [\freq, note, \atk, 0, \rel, 0.4, \amp, note, \busIn, nil, \busOut, bus1]);
				mod2 = Synth.after(mod1, \modulator, [\freq, note*0.4, \atk, 0, \rel, 0.4, \amp, note*0.4, \busIn, bus1, \busOut, bus2]);

				car = Synth.after(mod2, \sinCarrier, [\freq, note, \atk, 0, \rel, 0.4, \amp, master*0.9, \busIn, bus2, \pan, pan, \bufnum, f.bufnum, \bufnum2, f2.bufnum]);

				delta.yield;
			)
		}
	});

	~hh = Task({
		var bus1, bus2, bus3, delta, midi, dur, note, mod1, mod2, car;

		bus1 = Bus.audio(s, 1);
		bus2 = Bus.audio(s, 1);

		delta = 1;

		while {
			0 == 0;
		} {
			(
				note = 15000;

				mod1 = Synth.new(\modulator, [\freq, note, \atk, 0, \rel, 0.5, \amp, note*20, \busIn, nil, \busOut, bus1]);
				mod2 = Synth.after(mod1, \modulator, [\freq, note, \atk, 0, \rel, 0.5, \amp, note*20, \busIn, bus1, \busOut, bus2]);

				car = Synth.after(mod2, \sinCarrier, [\freq, note, \atk, 0, \rel, 0.3, \amp, master*0.04, \busIn, bus2, \pan, pan, \bufnum, f.bufnum, \bufnum2, f2.bufnum]);

				delta.yield;
			)
		}
	});


	b_demo = Button(w, Rect(10 + ((n+1)*j),10,100,20))
	.states_([
		["play demo", Color.black],
		["stop demo", Color.black],
	])
	.action_({ arg butt;
		if (butt.value == 1, {
			~bass.play(quant: TempoClock.default.beats + 1.0);
			~kick.play(quant: TempoClock.default.beats + 1.0);
			~hh.play(quant: TempoClock.default.beats + 1.5);
		});
		if (butt.value == 0, {
			~bass.stop;
			~kick.stop;
			~hh.stop;
			~bass.reset;
			~kick.reset;
			~hh.reset;
		});
	})
	.font = buttons_font;

	//function used to create a very simple display that will allow the user to see the topography of the whole FMSynth
	w.drawFunc = {
		//black box
		Pen.fillColor = Color.grey;
		Pen.moveTo((10 + ((n+1)*j))@210);
		Pen.lineTo((10 + ((n+1)*j))@410);
		Pen.lineTo((210 + ((n+1)*j))@410);
		Pen.lineTo((210 + ((n+1)*j))@210);
		Pen.lineTo((10 + ((n+1)*j))@210);
		Pen.draw(4); // fill and then stroke

		//link between first and third modulators
		if(mode_3 == 1, {
			Pen.moveTo((60 + ((n+1)*j))@260);
			Pen.lineTo((60 + ((n+1)*j))@310);
			Pen.draw(4);
		});


		//link between second and fourth modulators
		if(mode_4 == 1, {
			Pen.moveTo((160 + ((n+1)*j))@260);
			Pen.lineTo((160 + ((n+1)*j))@310);
			Pen.draw(4);
		});


		//link between (second+third modulators) and master
		if (mode_3 == 2, {
			Pen.fillColor = Color.grey;
			Pen.moveTo((100 + ((n+1)*j))@370);
			Pen.lineTo((40 + ((n+1)*j))@370);
			Pen.lineTo((40 + ((n+1)*j))@320);
			Pen.lineTo((50 + ((n+1)*j))@320);
			Pen.draw(4);
		});
		if (mode_4 == 2, {
			Pen.fillColor = Color.grey;
			Pen.moveTo((120 + ((n+1)*j))@370);
			Pen.lineTo((180 + ((n+1)*j))@370);
			Pen.lineTo((180 + ((n+1)*j))@320);
			Pen.lineTo((170 + ((n+1)*j))@320);
			Pen.draw(4);
		});
		Pen.moveTo((60 + ((n+1)*j))@330);
		Pen.lineTo((60 + ((n+1)*j))@345);
		Pen.draw(4);
		Pen.moveTo((160 + ((n+1)*j))@330);
		Pen.lineTo((160 + ((n+1)*j))@345);
		Pen.draw(4);
		Pen.moveTo((60 + ((n+1)*j))@345);
		Pen.lineTo((160 + ((n+1)*j))@345);
		Pen.draw(4);
		Pen.moveTo((110 + ((n+1)*j))@345);
		Pen.lineTo((110 + ((n+1)*j))@360);
		Pen.draw(4);

		//first modulator
		if(
			enabled_13 == 1,
			{
				if(
					mode_3 == 1,
					{Pen.fillColor = Color.green;}, //the modulator will have a green color when the left path is active and the input of the third modulator is set to 1, while it'll have a red color when not active
					{Pen.fillColor = Color.red;}
				);
			},
			{
				Pen.fillColor = Color.red;
			}
		);
		Pen.moveTo((50 + ((n+1)*j))@240);
		Pen.lineTo((50 + ((n+1)*j))@260);
		Pen.lineTo((70 + ((n+1)*j))@260);
		Pen.lineTo((70 + ((n+1)*j))@240);
		Pen.lineTo((50 + ((n+1)*j))@240);
		Pen.draw(4); // fill and then stroke

		Pen.moveTo((55 + ((n+1)*j))@246);
		Pen.lineTo((60 + ((n+1)*j))@243);
		Pen.lineTo((60 + ((n+1)*j))@257);
		Pen.draw(4);

		//second modulator
		if(
			enabled_24 == 1,
			{
				if(
					mode_4 == 1,
					{Pen.fillColor = Color.green;},   //the modulator will have a green color when the right path is active and the input of the foutrh modulator is set to 1, while it'll have a red color when not active
					{Pen.fillColor = Color.red;}
				);
			},
			{
				Pen.fillColor = Color.red;
			}
		);
		Pen.moveTo((150 + ((n+1)*j))@240);
		Pen.lineTo((150 + ((n+1)*j))@260);
		Pen.lineTo((170 + ((n+1)*j))@260);
		Pen.lineTo((170 + ((n+1)*j))@240);
		Pen.lineTo((150 + ((n+1)*j))@240);
		Pen.draw(4); // fill and then stroke

		Pen.moveTo((155 + ((n+1)*j))@243);
		Pen.lineTo((165 + ((n+1)*j))@243);
		Pen.lineTo((165 + ((n+1)*j))@249);
		Pen.lineTo((155 + ((n+1)*j))@249);
		Pen.lineTo((155 + ((n+1)*j))@257);
		Pen.lineTo((165 + ((n+1)*j))@257);
		Pen.draw(4);

		//third modulator
		if(
			enabled_13 == 1,
			{Pen.fillColor = Color.green;}, //the modulator will have a green color when active, while it'll have a red color when not active
			{Pen.fillColor = Color.red;}
		);
		Pen.moveTo((50 + ((n+1)*j))@310);
		Pen.lineTo((50 + ((n+1)*j))@330);
		Pen.lineTo((70 + ((n+1)*j))@330);
		Pen.lineTo((70 + ((n+1)*j))@310);
		Pen.lineTo((50 + ((n+1)*j))@310);
		Pen.draw(4); // fill and then stroke

		Pen.moveTo((55 + ((n+1)*j))@313);
		Pen.lineTo((65 + ((n+1)*j))@313);
		Pen.lineTo((65 + ((n+1)*j))@320);
		Pen.lineTo((55 + ((n+1)*j))@320);
		Pen.lineTo((65 + ((n+1)*j))@320);
		Pen.lineTo((65 + ((n+1)*j))@327);
		Pen.lineTo((55 + ((n+1)*j))@327);
		Pen.draw(4);

		//fourth modulator
		if(
			enabled_24 == 1,
			{Pen.fillColor = Color.green;},  //the modulator will have a green color when active, while it'll have a red color when not active
			{Pen.fillColor = Color.red;}
		);
		Pen.moveTo((150 + ((n+1)*j))@310);
		Pen.lineTo((150 + ((n+1)*j))@330);
		Pen.lineTo((170 + ((n+1)*j))@330);
		Pen.lineTo((170 + ((n+1)*j))@310);
		Pen.lineTo((150 + ((n+1)*j))@310);
		Pen.draw(4); // fill and then stroke

		Pen.moveTo((155 + ((n+1)*j))@313);
		Pen.lineTo((155 + ((n+1)*j))@320);
		Pen.lineTo((165 + ((n+1)*j))@320);
		Pen.lineTo((165 + ((n+1)*j))@313);
		Pen.lineTo((165 + ((n+1)*j))@327);
		Pen.draw(4);

		//master (carrier)
		Pen.fillColor = Color.green;
		Pen.moveTo((100 + ((n+1)*j))@360);
		Pen.lineTo((100 + ((n+1)*j))@380);
		Pen.lineTo((120 + ((n+1)*j))@380);
		Pen.lineTo((120 + ((n+1)*j))@360);
		Pen.lineTo((100 + ((n+1)*j))@360);
		Pen.draw(4); // fill and then stroke

		Pen.moveTo((115 + ((n+1)*j))@363);
		Pen.lineTo((105 + ((n+1)*j))@363);
		Pen.lineTo((105 + ((n+1)*j))@377);
		Pen.lineTo((115 + ((n+1)*j))@377);
		Pen.draw(4);
	};

	c.start;
	c2.start;

	CmdPeriod.doOnce({w.close});
});