clear all;
x=[-360:0.1:360];

k=1;
for x1=x
    mi_ld(k) = trapez(x1, -360, -360, -270, -225);
    mi_lsr(k) = trapez(x1, -270, -225, -180, -135);
    mi_lma(k) = trapez(x1, -180, -135, -90, -45);
    mi_oz(k) = trapez(x1, -90, -45, 45, 90);
    mi_pma(k) = trapez(x1, 45, 90, 135, 180);
    mi_psr(k) = trapez(x1, 135, 180, 225, 270);
    mi_pd(k) = trapez(x1, 225, 270, 360, 360);
    k = k+1;
end

plot(x, mi_ld, x, mi_lsr, x, mi_lma, x, mi_oz, x, mi_pma, x, mi_psr, x, mi_pd);
