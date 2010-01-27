function [y1, y2] = system_swiatla(x)
	mi_ld = trapez(x, -360, -360, -270, -225);
	mi_lsr = trapez(x, -270, -225, -180, -135);
	mi_lma = trapez(x, -180, -135, -90, -45);
	mi_oz = trapez(x, -90, -45, 45, 90);
	mi_pma = trapez(x, 45, 90, 135, 180);
	mi_psr = trapez(x, 135, 180, 225, 270);
	mi_pd = trapez(x, 225, 270, 360, 360);

	% suma = mi_ld + mi_lsr + mi_lma + mi_oz + mi_pma + mi_psr + mi_pd;
	suma = mi_ld + mi_lsr + mi_lma + mi_oz + mi_pma + mi_psr + mi_pd;

	if suma==0
	   y1 = 0;
           y2 = 0;
	else 
	   y1 = (mi_ld * -10 + mi_lsr * -10 + mi_lma * -10 + mi_oz * 0 + mi_pma * 10 + mi_psr * 20 + mi_pd * 30) / suma;
	   y2 = (mi_ld * 100 + mi_lsr * 100 + mi_lma * 100 + mi_oz * 0 + mi_pma * 100 + mi_psr * 100 + mi_pd * 100) / suma;
	end
end
