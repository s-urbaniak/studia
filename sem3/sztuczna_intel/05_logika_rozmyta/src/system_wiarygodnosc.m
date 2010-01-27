function y = system_wiarygodnosc(x)
	mi_maly  = trapez(x, 0, 0, 18, 21);
	mi_kszt  = trapez(x, 18, 21, 25, 30);
	mi_mlody  = trapez(x, 25, 30, 40, 45);
	mi_sredni  = trapez(x, 40, 45, 55, 60);
	mi_stary  = trapez(x, 55, 60, 150, 150);

	suma = mi_maly + mi_kszt + mi_mlody + mi_sredni + mi_stary;

	if suma==0
	   y = 0;
	else 
	   y = (mi_maly * 5 + mi_kszt * 15 + mi_mlody * 66 + mi_sredni * 100 + mi_stary * 15) / suma;
end
