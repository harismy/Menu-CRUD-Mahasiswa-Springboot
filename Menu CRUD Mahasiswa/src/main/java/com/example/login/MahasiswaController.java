package com.example.login;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.login.model.Mahasiswa;

import jakarta.validation.Valid;



@Controller
@RequestMapping("/mahasiswa")
public class MahasiswaController {
    private List<Mahasiswa> dataMahasiswa = new ArrayList<>();
    private List<Mahasiswa> dataDariFile = new ArrayList<>();
    private List<Mahasiswa> datahapustxt = new ArrayList<>();
    private  String filepath= "data.txt";

    // Nampilin daftar mahasiswa
    @GetMapping("/list")
    public String tampilkanMahasiswa(Model model) {
        model.addAttribute("mahasiswaList", dataMahasiswa);
        return "mahasiswa";
    }

    // nampilin file tambah
    @GetMapping("/tambah")
    public String formTambah(Model model) {
        model.addAttribute("mahasiswa", new Mahasiswa());
        return "tambah";
    }
    // nyimpen mahasiswa baru
    @PostMapping("/tambah")
    public String simpanMahasiswa(@Valid @ModelAttribute Mahasiswa mahasiswa, BindingResult result, Model model) {
        
        if(result.hasErrors()){
            return "tambah";
        }
        dataMahasiswa.add(mahasiswa);
        SimpanKeFile(mahasiswa);
        return "redirect:/mahasiswa/list";
    }

    // logika edit berdasarkan NIM (hanya arraylist)
    @GetMapping("/edit/{npm}")
    public String formEdit(@PathVariable String npm, Model model) {
        Mahasiswa mhs = cariMahasiswaByNim(npm);
        model.addAttribute("mahasiswa", mhs);
        return "edit";
    }

    // Simpan hasil edit
    @PostMapping("/edit")
    public String simpanEdit(@ModelAttribute Mahasiswa mahasiswa) {
        Mahasiswa existing = cariMahasiswaByNim(mahasiswa.getNpm());
        if (existing != null) {
            existing.setNama(mahasiswa.getNama());
            existing.setProdi(mahasiswa.getProdi());
            existing.setIpk(mahasiswa.getIpk());
        }
        return "redirect:/mahasiswa/list";
    }

    // Hapus mahasiswa berdasarkan NIM (hanya arraylist)
    @GetMapping("/hapus/{npm}")
    public String hapusMahasiswa(@PathVariable String npm) {
        Mahasiswa mhs = cariMahasiswaByNim(npm);
        if (mhs != null) {
            dataMahasiswa.remove(mhs);
        }
        return "redirect:/mahasiswa/list";
    }

    // controller pembaca file txt
    @GetMapping("/lihatdata")
    public String lihatdatamhs(Model model){

        dataDariFile.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))){
            String baris;
            while((baris=reader.readLine()) !=null){
                String [] data = baris.split("\\|");
                if(data.length == 4){
                    dataDariFile.add(new Mahasiswa(data[0], data[1], data[2], data[3]));
                }
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan ketika membaca file"+ e.getMessage());
        }
        model.addAttribute("mahasiswaList", dataDariFile);
        return "datatxt";
    }

    // Util: cari mahasiswa berdasarkan NIM
    private Mahasiswa cariMahasiswaByNim(String npm) {
        for (Mahasiswa m : dataMahasiswa) {
            if (m.getNpm().equals(npm)) {
                return m;
            }
        }
        return null;
    }
    //logika pembuat isi file txt
    private void SimpanKeFile(Mahasiswa mhs){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true))){
            String data = mhs.getNpm()+"|"+ mhs.getNama()+"|"+ mhs.getProdi()+"|"+mhs.getIpk();
            writer.write(data);
            writer.newLine();
        } catch (Exception e) {
            System.out.println("Gagal menyimpan data kedalam file"+e.getMessage());
        }
    }

    //controller penghapus isi file txt
    @GetMapping("/hapusisitxt/{npm}")
    public String hapustxt(@PathVariable String npm){

        datahapustxt.clear();
        try (BufferedReader hpstxt = new BufferedReader(new FileReader(filepath))) {
            String baris;
            while((baris = hpstxt.readLine())!=null){
                String [] data = baris.split("\\|");
                if(data.length == 4 ){
                    Mahasiswa mhs = new Mahasiswa (data[0], data[1], data[2], data[3]);
                    datahapustxt.add(mhs);
                }
            }
        } catch (Exception e) {
            System.out.println("Gagal menghapus data "+e.getMessage());
        }
    datahapustxt.removeIf(m -> m.getNpm().trim().equals(npm.trim()));
    
    // Tulis ulang file tanpa data yang dihapus buat hapus data yang ada di dalam file txt
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, false))) {
        for (Mahasiswa mhs : datahapustxt) {
            String data = mhs.getNpm() + "|" + mhs.getNama() + "|" + mhs.getProdi() + "|" + mhs.getIpk();
            writer.write(data);
            writer.newLine();
        }
    } catch (Exception e) {
        System.out.println("Gagal menulis ulang file: " + e.getMessage());
    }

        return "redirect:/mahasiswa/lihatdata";
    }
    //controller edit file txt
    @GetMapping("/editfile/{npm}")
    public String EditTxt(@PathVariable String npm, Model model){
        Mahasiswa mahasiswaDitemukan = null;

        try (BufferedReader reader= new BufferedReader(new FileReader(filepath))){
            String baris;
            while((baris= reader.readLine())!=null){
                String [] data = baris.split("\\|");
                if(data.length == 4 && data[0].trim().equals(npm.trim())){
                    mahasiswaDitemukan = new Mahasiswa(data[0], data[1], data[2], data[3]);
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Gagal membaca file"+e.getMessage());
        }
        if(mahasiswaDitemukan == null){
            return "redirect:/mahasiswa/lihatdata";
        }
        model.addAttribute("mahasiswa", mahasiswaDitemukan);
        return "editfiletxt";
    }
    @PostMapping("/editfile")
    public String simpanEditToFile(@ModelAttribute Mahasiswa mahasiswa) {
    List<Mahasiswa> listBaru = new ArrayList<>();

    listBaru.clear();
    // Baca semua data dari file
    try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
        String baris;
        while ((baris = reader.readLine()) != null) {
            String[] data = baris.split("\\|");
            if (data.length == 4) {
                if (data[0].trim().equals(mahasiswa.getNpm().trim())) {
                    // Ganti dengan data baru
                    listBaru.add(mahasiswa);
                } else {
                    listBaru.add(new Mahasiswa(data[0], data[1], data[2], data[3]));
                }
            }
        }
    } catch (Exception e) {
        System.out.println("Gagal membaca file untuk update: " + e.getMessage());
    }

    // Tulis ulang file dengan data yang sudah diperbarui
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
        for (Mahasiswa m : listBaru) {
            String line = m.getNpm() + "|" + m.getNama() + "|" + m.getProdi() + "|" + m.getIpk();
            writer.write(line);
            writer.newLine();
        }
    } catch (Exception e) {
        System.out.println("Gagal menyimpan data ke file: " + e.getMessage());
    }

    return "redirect:/mahasiswa/lihatdata";
}
}