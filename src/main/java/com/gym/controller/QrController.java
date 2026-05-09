package com.gym.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.gym.entity.Client;
import com.gym.service.CardService;
import com.gym.service.ClientService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayOutputStream;

@Controller
@RequiredArgsConstructor
public class QrController {

    private final ClientService clientService;
    private final CardService cardService;

    /** Returns a PNG QR image encoding the client's personal card URL. */
    @GetMapping("/clients/{id}/qr")
    @ResponseBody
    public ResponseEntity<byte[]> qrImage(@PathVariable Long id,
                                          HttpServletRequest request) throws Exception {
        Client client = clientService.findById(id);

        String baseUrl = request.getScheme() + "://" + request.getServerName()
                + (request.getServerPort() == 80 || request.getServerPort() == 443
                ? "" : ":" + request.getServerPort());

        BitMatrix matrix = new QRCodeWriter()
                .encode(baseUrl + "/my/" + client.getToken(), BarcodeFormat.QR_CODE, 300, 300);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(out.toByteArray());
    }

    /** Mobile-friendly client card page opened after scanning the QR. */
    @GetMapping("/my/{token}")
    public String myCard(@PathVariable String token, Model model) {
        return clientService.findByToken(token)
                .map(client -> {
                    model.addAttribute("client", client);
                    model.addAttribute("card", cardService.findActiveCardByClient(client));
                    return "my/card";
                })
                .orElse("my/not-found");
    }
}
