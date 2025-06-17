package com.example.login.model;
import jakarta.validation.constraints.NotBlank;

public class Mahasiswa{
    @NotBlank(message = "NPM tidak boleh kosong")
    private String npm;

    @NotBlank(message = "Nama tidak boleh kosong")
    private String nama;

    @NotBlank(message = "Prodi tidak boleh kosong")
    private String prodi;

    @NotBlank(message = "IPK tidak boleh kosong")
    private String ipk;

   public Mahasiswa(){
        
    }
   public Mahasiswa(String npm, String nama, String prodi, String ipk){
        this.nama = nama;
        this.npm = npm;
        this.prodi = prodi;
        this.ipk = ipk;
    }
    public void TampilData(){
        System.out.println("Nama : "+nama);
        System.out.println("NPM  : "+npm);
        System.out.println("Prodi: "+prodi);
        System.out.println("IPK  : "+ipk);
    }
    @Override
    public String toString() {
        return "Nama : " + nama + "\n" +
               "NPM  : " + npm + "\n" +
               "Prodi: " + prodi + "\n" +
               "IPK  : " + ipk + "\n";
    }
    public String getNama(){
        return nama;
    }
    public void setNama(String nama){
        this.nama = nama;
    }
    public String getNpm(){
    return npm;
    }
    public void setNpm(String npm){
        this.npm = npm;
    }
    public String getProdi(){
        return prodi;
    }
    public void setProdi(String prodi){
        this.prodi = prodi;
    }
    public String getIpk(){
        return ipk;
    }
    public void setIpk(String ipk){
        this.ipk = ipk;
    }
}
