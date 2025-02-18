package com.eternity.datasiswa;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.Objects;

public class siswaAdapter extends CursorAdapter {

    public siswaAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.siswa_listview_items, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        GridLayout layout = view.findViewById(R.id.listView_Item_layout);
        TextView noID = view.findViewById(R.id.noID);
        TextView nama = view.findViewById(R.id.nama);
        TextView ratarata = view.findViewById(R.id.ratarata);
        int position = cursor.getPosition();

        if ((position) % 2 == 0) {
            layout.setBackgroundColor(ContextCompat.getColor(context, R.color.columnGanjil));
        } else {
            layout.setBackgroundColor(ContextCompat.getColor(context, R.color.columnGenap));
        }

        noID.setText(String.valueOf(position + 1));
        String namaSiswa = cursor.getString(cursor.getColumnIndexOrThrow("nama"));
        nama.setText(namaSiswa);
        int siswaID = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        int harian = cursor.getInt(cursor.getColumnIndexOrThrow("nilaiHarian"));
        int tugas = cursor.getInt(cursor.getColumnIndexOrThrow("nilaiTugas"));
        int akhir = cursor.getInt(cursor.getColumnIndexOrThrow("nilaiUlangan"));
        ratarata.setText(String.valueOf((harian + tugas + akhir) / 3));

        layout.setOnClickListener(o -> {
            siswaDatabase db = new siswaDatabase(context);

            Dialog details = new Dialog(context);
            details.setContentView(R.layout.siswa_details);
            details.setCancelable(true);
            Objects.requireNonNull(details.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            ImageButton siswaDelete = details.findViewById(R.id.deleteSiswa);
            ImageButton siswaeditData = details.findViewById(R.id.editSiswa);
            ImageButton closeDialog = details.findViewById(R.id.closeDialog);

            TextView namaDetail = details.findViewById(R.id.detailNama);
            TextView harianDetail = details.findViewById(R.id.detailHarian);
            TextView tugasDetail = details.findViewById(R.id.detailTugas);
            TextView ulanganDetail = details.findViewById(R.id.detailUlangan);

            namaDetail.setText(namaSiswa);
            harianDetail.setText(String.valueOf(harian));
            tugasDetail.setText(String.valueOf(tugas));
            ulanganDetail.setText(String.valueOf(akhir));

            siswaDelete.setOnClickListener(d -> {
                Dialog confirm = new Dialog(context);
                confirm.setContentView(R.layout.confirm_delete);
                confirm.setCancelable(true);
                Objects.requireNonNull(confirm.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                Button cancelDelete = confirm.findViewById(R.id.cancelDelete);
                Button confirmDelete = confirm.findViewById(R.id.confirmDelete);

                cancelDelete.setOnClickListener(cd -> {
                    confirm.cancel();
                });

                if (context instanceof Activity) {
                    final Activity activity = (Activity) context;
                    confirmDelete.setOnClickListener(cds -> {
                        db.deleteSiswa(siswaID);
                        activity.recreate();
                    });
                }

                confirm.show();
            });

            siswaeditData.setOnClickListener(se -> {
                Dialog editData = new Dialog(context);
                editData.setContentView(R.layout.update_layout);
                editData.setCancelable(true);
                Objects.requireNonNull(editData.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                ImageButton closeEditDialog = editData.findViewById(R.id.closeDialog);
                EditText inputNama = editData.findViewById(R.id.inputnama);
                EditText inputNilaiHarian = editData.findViewById(R.id.nilaiHarian);
                EditText inputNilaiTugas = editData.findViewById(R.id.nilaiTugas);
                EditText inputNilaiAkhir = editData.findViewById(R.id.nilaiUlangan);
                Button cancelUpdate = editData.findViewById(R.id.cancelUpdate);
                Button submitButton = editData.findViewById(R.id.updateButton);

                inputNama.setText(namaSiswa);
                inputNilaiHarian.setText(String.valueOf(harian));
                inputNilaiTugas.setText(String.valueOf(tugas));
                inputNilaiAkhir.setText(String.valueOf(akhir));

                inputNilaiHarian.setFilters(new InputFilter[]{new InputFilterMinMax(0,100)});
                inputNilaiTugas.setFilters(new InputFilter[]{new InputFilterMinMax(0,100)});
                inputNilaiAkhir.setFilters(new InputFilter[]{new InputFilterMinMax(0,100)});

                closeEditDialog.setOnClickListener(ced -> { editData.cancel(); });

                cancelUpdate.setOnClickListener(cud -> { editData.cancel(); });

                if (context instanceof Activity) {
                    final Activity activity = (Activity) context;
                    submitButton.setOnClickListener(sbtn -> {
                        String updateNama = inputNama.getText().toString();
                        String updateHarian = inputNilaiHarian.getText().toString();
                        String updateTugas = inputNilaiTugas.getText().toString();
                        String updateUlangan = inputNilaiAkhir.getText().toString();

                        int harianInt = Integer.parseInt(updateHarian);
                        int tugasInt = Integer.parseInt(updateTugas);
                        int akhirInt = Integer.parseInt(updateUlangan);

                        db.updateSiswa(updateNama,harianInt,tugasInt,akhirInt,siswaID);
                        activity.recreate();
                    });
                }

                editData.show();
            });

            closeDialog.setOnClickListener(e -> {
                details.cancel();
            });

            details.show();
        });
    }
}
