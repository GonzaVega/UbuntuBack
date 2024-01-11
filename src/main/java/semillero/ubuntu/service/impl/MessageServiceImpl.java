package semillero.ubuntu.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import semillero.ubuntu.entities.Message;
import semillero.ubuntu.entities.Microentrepreneurship;
import semillero.ubuntu.enums.Management;
import semillero.ubuntu.repository.MessageRepository;
import semillero.ubuntu.repository.MicroentrepreneurshipRepository;
import semillero.ubuntu.service.MailService;
import semillero.ubuntu.service.contract.MessageService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {

    private final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    private MicroentrepreneurshipRepository microentrepreneurshipRepository;

    @Autowired
    private MailService emailService;

    @Autowired
    private UserServiceImpl userService;


    @Value("${frontend.url}")
    private String FRONT_URL;

    @Override
    public Message saveMessage(Long microentrepreneurshipId, Message message) {
        logger.info("Save Message");

        // Verifica si el microemprendimiento existe
        Microentrepreneurship microentrepreneurship = microentrepreneurshipRepository.findById(microentrepreneurshipId)
                .orElseThrow(() -> new EntityNotFoundException("The microentrepreneirship with the ID: " + microentrepreneurshipId + " was not found."));

        // Asocia el mensaje con el microemprendimiento
        message.setMicroentrepreneurship(microentrepreneurship);


        // Envia un correo electrónico a todos los usuarios con el rol "admin"
        List<String> adminUsers = userService.getAllAdminEmails();

        Map<String, Object> emailData = new HashMap<>();
        emailData.put("fullName", message.getFullName());
        emailData.put("microentrepreneurshipName", message.getMicroentrepreneurship().getName());
        emailData.put("messageContent", message.getMessage());


        String content = createMessageTemplate(message.getFullName(), message.getPhone(), message.getEmail(), message.getMicroentrepreneurship().getName(), message.getMessage());

        for (String adminEmail : adminUsers) {
            emailService.sendEmail(adminEmail, "Contacto inversionista", content);
        }

        // Guarda el mensaje
        return messageRepository.save(message);
    }

    // método para crear plantilla para el correo
    public String createMessageTemplate(String fullName, String phone, String email, String microentrepreneurshipName, String messageContent) {
        String stringUrl = FRONT_URL + "/admin/solicitudes-de-contacto";

        String firstPart = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html dir=\"ltr\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" lang=\"en\" style=\"padding:0;Margin:0\">\n" +
                " <head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta content=\"width=device-width, initial-scale=1\" name=\"viewport\">\n" +
                "  <meta name=\"x-apple-disable-message-reformatting\">\n" +
                "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "  <meta content=\"telephone=no\" name=\"format-detection\">\n" +
                "  <title>Nueva plantilla</title><!--[if (mso 16)]>\n" +
                "    <style type=\"text/css\">\n" +
                "    a {text-decoration: none;}\n" +
                "    </style>\n" +
                "    <![endif]--><!--[if gte mso 9]><style>sup { font-size: 100% !important; }</style><![endif]--><!--[if gte mso 9]>\n" +
                "<xml>\n" +
                "    <o:OfficeDocumentSettings>\n" +
                "    <o:AllowPNG></o:AllowPNG>\n" +
                "    <o:PixelsPerInch>96</o:PixelsPerInch>\n" +
                "    </o:OfficeDocumentSettings>\n" +
                "</xml>\n" +
                "<![endif]-->\n" +
                "  <style type=\"text/css\">\n" +
                "#outlook a {\n" +
                "\tpadding:0;\n" +
                "}\n" +
                ".ExternalClass {\n" +
                "\twidth:100%;\n" +
                "}\n" +
                ".ExternalClass,\n" +
                ".ExternalClass p,\n" +
                ".ExternalClass span,\n" +
                ".ExternalClass font,\n" +
                ".ExternalClass td,\n" +
                ".ExternalClass div {\n" +
                "\tline-height:100%;\n" +
                "}\n" +
                ".es-button {\n" +
                "\tmso-style-priority:100!important;\n" +
                "\ttext-decoration:none!important;\n" +
                "}\n" +
                "a[x-apple-data-detectors] {\n" +
                "\tcolor:inherit!important;\n" +
                "\ttext-decoration:none!important;\n" +
                "\tfont-size:inherit!important;\n" +
                "\tfont-family:inherit!important;\n" +
                "\tfont-weight:inherit!important;\n" +
                "\tline-height:inherit!important;\n" +
                "}\n" +
                ".es-desk-hidden {\n" +
                "\tdisplay:none;\n" +
                "\tfloat:left;\n" +
                "\toverflow:hidden;\n" +
                "\twidth:0;\n" +
                "\tmax-height:0;\n" +
                "\tline-height:0;\n" +
                "\tmso-hide:all;\n" +
                "}\n" +
                "@media only screen and (max-width:600px) {p, ul li, ol li, a { line-height:150%!important } h1, h2, h3, h1 a, h2 a, h3 a { line-height:120%!important } h1 { font-size:30px!important; text-align:center } h2 { font-size:26px!important; text-align:center } h3 { font-size:20px!important; text-align:center } h1 a { text-align:center } .es-header-body h1 a, .es-content-body h1 a, .es-footer-body h1 a { font-size:30px!important } h2 a { text-align:center } .es-header-body h2 a, .es-content-body h2 a, .es-footer-body h2 a { font-size:24px!important } h3 a { text-align:center } .es-header-body h3 a, .es-content-body h3 a, .es-footer-body h3 a { font-size:20px!important } .es-menu td a { font-size:16px!important } .es-header-body p, .es-header-body ul li, .es-header-body ol li, .es-header-body a { font-size:16px!important } .es-content-body p, .es-content-body ul li, .es-content-body ol li, .es-content-body a { font-size:16px!important } .es-footer-body p, .es-footer-body ul li, .es-footer-body ol li, .es-footer-body a { font-size:16px!important } .es-infoblock p, .es-infoblock ul li, .es-infoblock ol li, .es-infoblock a { font-size:12px!important } *[class=\"gmail-fix\"] { display:none!important } .es-m-txt-c, .es-m-txt-c h1, .es-m-txt-c h2, .es-m-txt-c h3 { text-align:center!important } .es-m-txt-r, .es-m-txt-r h1, .es-m-txt-r h2, .es-m-txt-r h3 { text-align:right!important } .es-m-txt-l, .es-m-txt-l h1, .es-m-txt-l h2, .es-m-txt-l h3 { text-align:left!important } .es-m-txt-r img, .es-m-txt-c img, .es-m-txt-l img { display:inline!important } .es-button-border { display:block!important } a.es-button, button.es-button { font-size:20px!important; display:block!important; padding:10px 0px 10px 0px!important } .es-btn-fw { border-width:10px 0px!important; text-align:center!important } .es-adaptive table, .es-btn-fw, .es-btn-fw-brdr, .es-left, .es-right { width:100%!important } .es-content table, .es-header table, .es-footer table, .es-content, .es-footer, .es-header { width:100%!important; max-width:600px!important } .es-adapt-td { display:block!important; width:100%!important } .adapt-img { width:100%!important; height:auto!important } .es-m-p0 { padding:0px!important } .es-m-p0r { padding-right:0px!important } .es-m-p0l { padding-left:0px!important } .es-m-p0t { padding-top:0px!important } .es-m-p0b { padding-bottom:0!important } .es-m-p20b { padding-bottom:20px!important } .es-mobile-hidden, .es-hidden { display:none!important } tr.es-desk-hidden, td.es-desk-hidden, table.es-desk-hidden { width:auto!important; overflow:visible!important; float:none!important; max-height:inherit!important; line-height:inherit!important } tr.es-desk-hidden { display:table-row!important } table.es-desk-hidden { display:table!important } td.es-desk-menu-hidden { display:table-cell!important } .es-menu td { width:1%!important } table.es-table-not-adapt, .esd-block-html table { width:auto!important } table.es-social { display:inline-block!important } table.es-social td { display:inline-block!important } .es-desk-hidden { display:table-row!important; width:auto!important; overflow:visible!important; max-height:inherit!important } }\n" +
                "@media screen and (max-width:384px) {.mail-message-content { width:414px!important } }\n" +
                "</style>\n" +
                " </head>\n" +
                " <body style=\"width:100%;font-family:tahoma, verdana, segoe, sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0\">\n" +
                "  <div dir=\"ltr\" class=\"es-wrapper-color\" lang=\"en\" style=\"background-color:#E8E8E4\"><!--[if gte mso 9]>\n" +
                "\t\t\t<v:background xmlns:v=\"urn:schemas-microsoft-com:vml\" fill=\"t\">\n" +
                "\t\t\t\t<v:fill type=\"tile\" color=\"#e8e8e4\"></v:fill>\n" +
                "\t\t\t</v:background>\n" +
                "\t\t<![endif]-->\n" +
                "   <table class=\"es-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;padding:0;Margin:0;width:100%;height:100%;background-repeat:repeat;background-position:center top;background-color:#E8E8E4\">\n" +
                "     <tr style=\"border-collapse:collapse\">\n" +
                "      <td valign=\"top\" style=\"padding:0;Margin:0\">\n" +
                "       <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\">\n" +
                "         <tr style=\"border-collapse:collapse\">\n" +
                "          <td class=\"es-adaptive\" align=\"center\" style=\"padding:0;Margin:0\">\n" +
                "           <table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;width:600px\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">\n" +
                "             <tr style=\"border-collapse:collapse\">\n" +
                "              <td align=\"left\" style=\"padding:10px;Margin:0\">\n" +
                "               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                 <tr style=\"border-collapse:collapse\">\n" +
                "                  <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:580px\">\n" +
                "                   <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                     <tr style=\"border-collapse:collapse\">\n" +
                "                      <td esdev-links-color=\"#999999\" align=\"left\" class=\"es-infoblock\" style=\"padding:0;Margin:0;line-height:14px;font-size:12px;color:#999999\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:tahoma, verdana, segoe, sans-serif;line-height:14px;color:#999999;font-size:12px\">Un emprendimiento espera ser gestionado..<span style=\"text-align:center;line-height:150%\">. </span><a class=\"view\" target=\"_blank\" href=\"https://viewstripo.email/\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;text-decoration:underline;color:#999999;font-size:12px\">Ver en el navegador</a></p></td>\n" +
                "                     </tr>\n" +
                "                   </table></td>\n" +
                "                 </tr>\n" +
                "               </table></td>\n" +
                "             </tr>\n" +
                "           </table></td>\n" +
                "         </tr>\n" +
                "       </table>\n" +
                "       <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-header\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;background-color:transparent;background-repeat:repeat;background-position:center top\">\n" +
                "         <tr style=\"border-collapse:collapse\">\n" +
                "          <td align=\"center\" style=\"padding:0;Margin:0\">\n" +
                "           <table class=\"es-header-body\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;width:600px\">\n" +
                "             <tr style=\"border-collapse:collapse\">\n" +
                "              <td align=\"left\" style=\"Margin:0;padding-top:10px;padding-left:15px;padding-right:15px;padding-bottom:20px\">\n" +
                "               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                 <tr style=\"border-collapse:collapse\">\n" +
                "                  <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:570px\">\n" +
                "                   <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                     <tr style=\"border-collapse:collapse\">\n" +
                "                      <td class=\"es-m-p0l\" align=\"center\" style=\"padding:0;Margin:0;font-size:0\"><a href=\"https://viewstripo.email\" target=\"_blank\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;text-decoration:underline;color:#999999;font-size:14px\"><img src=\"https://res.cloudinary.com/dworm9bnx/image/upload/v1704985184/Ubuntu_Marcas-01_au4xyi.webp\" alt=\"Bookkeeping logo\" title=\"Ubuntu logo\" width=\"113\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\"></a></td>\n" +
                "                     </tr>\n" +
                "                   </table></td>\n" +
                "                 </tr>\n" +
                "               </table></td>\n" +
                "             </tr>\n" +
                "           </table></td>\n" +
                "         </tr>\n" +
                "       </table>\n" +
                "       <table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\">\n" +
                "         <tr style=\"border-collapse:collapse\">\n" +
                "          <td class=\"es-adaptive\" align=\"center\" style=\"padding:0;Margin:0\">\n" +
                "           <table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#8796a3;width:600px\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#8796a3\" align=\"center\">\n" +
                "             <tr style=\"border-collapse:collapse\">\n" +
                "              <td align=\"left\" style=\"padding:0;Margin:0;padding-top:25px\">\n" +
                "               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                 <tr style=\"border-collapse:collapse\">\n" +
                "                  <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:600px\">\n" +
                "                   <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                     <tr style=\"border-collapse:collapse\">\n" +
                "                      <td align=\"center\" style=\"padding:0;Margin:0;padding-bottom:30px\"><h1 style=\"Margin:0;line-height:36px;mso-line-height-rule:exactly;font-family:tahoma, verdana, segoe, sans-serif;font-size:30px;font-style:normal;font-weight:normal;color:#ffffff\">UBUNTU</h1></td>\n" +
                "                     </tr>\n" +
                "                     <tr style=\"border-collapse:collapse; color:\">\n" +
                "                      <td align=\"center\" style=\"padding:0;Margin:0;font-size:0\"><a target=\"_blank\" href=\"https://viewstripo.email\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;text-decoration:underline;color:#02951E;font-size:14px\"><img class=\"adapt-img\" src=\"https://i0.wp.com/diariosanrafael.com.ar/wp-content/uploads/2020/08/curso-online-de-agroecologia_amp_primaria_1_1560503079.jpg?fit=1024%2C1024&ssl=1\" alt=\"Clock\" title=\"Clock\" width=\"600\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\"></a></td>\n" +
                "                     </tr>\n" +
                "                   </table></td>\n" +
                "                 </tr>\n" +
                "               </table></td>\n" +
                "             </tr>\n" +
                "           </table></td>\n" +
                "         </tr>\n" +
                "       </table>\n" +
                "       <table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\">\n" +
                "         <tr style=\"border-collapse:collapse\">\n" +
                "          <td align=\"center\" style=\"padding:0;Margin:0\">\n" +
                "           <table class=\"es-content-body\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FFFFFF;width:600px\">\n" +
                "             <tr style=\"border-collapse:collapse\">\n" +
                "              <td align=\"left\" style=\"Margin:0;padding-bottom:10px;padding-left:30px;padding-right:30px;padding-top:40px\">\n" +
                "               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                 <tr style=\"border-collapse:collapse\">\n" +
                "                  <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:540px\">\n" +
                "                   <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                     <tr style=\"border-collapse:collapse\">\n" +
                "                      <td class=\"es-m-txt-c\" align=\"left\" style=\"padding:0;Margin:0\"><h3 style=\"Margin:0;line-height:24px;mso-line-height-rule:exactly;font-family:tahoma, verdana, segoe, sans-serif;font-size:20px;font-style:normal;font-weight:normal;color:#333333\">Hola administrador UBUNTU, tenemos buenas noticias!<br></h3></td>\n" +
                "                     </tr>\n" +
                "                     <tr style=\"border-collapse:collapse\">\n" +
                "                      <td class=\"es-m-txt-c\" align=\"left\" style=\"padding:0;Margin:0;padding-top:10px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:tahoma, verdana, segoe, sans-serif;line-height:21px;color:#666666;font-size:14px\">Alguien ha expresado su interés de invertir en "+ microentrepreneurshipName+" y este es el mensaje que ha dejado:<br></p></td>\n\n" +
                "                     </tr>\n" +
                "                     <tr style=\"border-collapse:collapse\">\n" +
                "                      <td esdev-links-color=\"#50b948\" class=\"es-m-txt-c\" align=\"left\" style=\"padding:0;Margin:0;padding-top:5px;padding-bottom:10px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:tahoma, verdana, segoe, sans-serif;line-height:21px;color:#666666;font-size:14px\">\n"+ messageContent +
                "                     </tr>\n" +
                "                     <tr style=\"border-collapse:collapse\">\n" +
                "                      <td class=\"es-m-txt-c\" align=\"left\" style=\"padding:0;Margin:0;padding-top:10px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:tahoma, verdana, segoe, sans-serif;line-height:21px;color:#666666;font-size:14px\">Puedes contactarle a través de su número teléfónico: "+ phone +" o por medio de su correo: "+ email +". Recuerda seguir las mejores prácticas de seguridad y privacidad al gestionar esta información. Si necesitas más detalles o asistencia sobre la correcta gestión de un microemprendimiento, revisa el siguiente enlace: <a target=\"_blank\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;text-decoration:underline;color:#50b948;font-size:14px\" href=\"https://viewstripo.email/\">Gestión de los microemprendimientos</a>.<br></p></td><br></p></td>\n" +
                "                     </tr>\n" +
                "                     <tr style=\"border-collapse:collapse\">\n" +
                "                      <td align=\"left\" style=\"padding:0;Margin:0;padding-right:10px;padding-top:20px;padding-bottom:20px\">";

        StringBuilder secondPart = new StringBuilder();
        secondPart.append("<span class=\"es-button-border\" style=\"border-style:solid;border-color:#50B948;background:#2CB543;border-width:0px;display:inline-block;border-radius:4px;width:auto\">");
        secondPart.append("<a href=\"").append(stringUrl).append("\" class=\"es-button\" target=\"_blank\" style=\"mso-style-priority:100 !important;text-decoration:none;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;color:#FFFFFF;font-size:16px;display:inline-block;background:#50B948;border-radius:4px;font-family:arial, 'helvetica neue', helvetica, sans-serif;font-weight:normal;font-style:normal;line-height:19px;width:auto;text-align:center;padding:10px 20px 10px 20px;mso-padding-alt:0;mso-border-alt:10px solid #50B948\">Gestiona este emprendimiento/a></span>");

        String thirdPart = "                     </tr>\n" +
                "                   </table></td>\n" +
                "                 </tr>\n" +
                "               </table></td>\n" +
                "             </tr>\n" +
                "             <tr style=\"border-collapse:collapse\">\n" +
                "              <td align=\"left\" style=\"Margin:0;padding-top:20px;padding-left:30px;padding-right:30px;padding-bottom:40px\"><!--[if mso]><table style=\"width:540px\" cellpadding=\"0\" cellspacing=\"0\"><tr><td style=\"width:260px\" valign=\"top\"><![endif]-->\n" +
                "               <table class=\"es-left\" cellspacing=\"0\" cellpadding=\"0\" align=\"left\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;float:left\">\n" +
                "                 <tr style=\"border-collapse:collapse\">\n" +
                "                  <td class=\"es-m-p20b\" align=\"left\" style=\"padding:0;Margin:0;width:260px\">\n" +
                "                   <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                     <tr style=\"border-collapse:collapse\">\n" +
                "                      <td align=\"left\" style=\"padding:0;Margin:0\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:tahoma, verdana, segoe, sans-serif;line-height:21px;color:#666666;font-size:14px\"><span style=\"font-weight:bold;line-height:150%\">Equipo UBUNTU</span>, financiamiento sostenible<br></p></td>\n" +
                "                     </tr>\n" +
                "                   </table></td>\n" +
                "                 </tr>\n" +
                "               </table><!--[if mso]></td><td style=\"width:20px\"></td><td style=\"width:260px\" valign=\"top\"><![endif]-->\n" +
                "               <table class=\"es-right\" cellspacing=\"0\" cellpadding=\"0\" align=\"right\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;float:right\">\n" +
                "                 <tr style=\"border-collapse:collapse\">\n" +
                "                  <td align=\"left\" style=\"padding:0;Margin:0;width:260px\">\n" +
                "                   <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                     <tr style=\"border-collapse:collapse\">\n" +
                "                      <td align=\"right\" style=\"padding:0;Margin:0\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:tahoma, verdana, segoe, sans-serif;line-height:21px;color:#666666;font-size:14px\"></p></td>\n" +
                "                     </tr>\n" +
                "                   </table></td>\n" +
                "                 </tr>\n" +
                "               </table><!--[if mso]></td></tr></table><![endif]--></td>\n" +
                "             </tr>\n" +
                "           </table></td>\n" +
                "         </tr>\n" +
                "       </table>\n" +
                "       <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-footer\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;background-color:transparent;background-repeat:repeat;background-position:center top\">\n" +
                "         <tr style=\"border-collapse:collapse\">\n" +
                "          <td align=\"center\" style=\"padding:0;Margin:0\">\n" +
                "           <table class=\"es-footer-body\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;width:600px\">\n" +
                "             <tr style=\"border-collapse:collapse\">\n" +
                "              <td align=\"left\" style=\"padding:0;Margin:0\">\n" +
                "               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                 <tr style=\"border-collapse:collapse\">\n" +
                "                  <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:600px\">\n" +
                "                   <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                     <tr style=\"border-collapse:collapse\">\n" +
                "                      <td align=\"center\" style=\"padding:0;Margin:0;font-size:0\">\n" +
                "                       <table width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                         <tr style=\"border-collapse:collapse\">\n" +
                "                          <td style=\"padding:0;Margin:0;border-bottom:4px solid #cccccc;background:#FFFFFF none repeat scroll 0% 0%;height:1px;width:100%;margin:0px\"></td>\n" +
                "                         </tr>\n" +
                "                       </table></td>\n" +
                "                     </tr>\n" +
                "                   </table></td>\n" +
                "                 </tr>\n" +
                "               </table></td>\n" +
                "             </tr>\n" +
                "             <tr style=\"border-collapse:collapse\">\n" +
                "              <td align=\"left\" style=\"padding:0;Margin:0;padding-top:30px;padding-left:30px;padding-right:30px\">\n" +
                "               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                 <tr style=\"border-collapse:collapse\">\n" +
                "                  <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:540px\">\n" +
                "                   <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                     <tr style=\"border-collapse:collapse\">\n" +
                "                      <td align=\"center\" style=\"padding:0;Margin:0;padding-bottom:10px;font-size:0\"><a target=\"_blank\" href=\"https://viewstripo.email\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;text-decoration:underline;color:#989898;font-size:14px\"><img class=\"adapt-img\" src=\"https://fbxcbof.stripocdn.email/content/guids/CABINET_149443347669cc74a67164f50eff1380/images/53081509623983563.png\" alt=\"Bookkeeping logo\" title=\"Bookkeeping logo\" width=\"115\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\"></a></td>\n" +
                "                     </tr>\n" +
                "                     <tr style=\"border-collapse:collapse\">\n" +
                "                      <td align=\"center\" style=\"padding:0;Margin:0\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:tahoma, verdana, segoe, sans-serif;line-height:21px;color:#989898;font-size:14px\">This email was sent by Bookkeeping Inc.</p><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:tahoma, verdana, segoe, sans-serif;line-height:21px;color:#989898;font-size:14px\"><a target=\"_blank\" href=\"https://viewstripo.email\" class=\"view\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;text-decoration:underline;color:#989898;font-size:14px\">View in browser</a> | <a target=\"_blank\" href=\"\" class=\"unsubscribe\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;text-decoration:underline;color:#989898;font-size:14px\">Unsubscribe</a></p></td>\n" +
                "                     </tr>\n" +
                "                     <tr style=\"border-collapse:collapse\">\n" +
                "                      <td align=\"center\" style=\"padding:0;Margin:0;padding-bottom:5px;padding-top:15px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:tahoma, verdana, segoe, sans-serif;line-height:21px;color:#989898;font-size:14px\">© Bookkeeping Inc.</p><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:tahoma, verdana, segoe, sans-serif;line-height:21px;color:#989898;font-size:14px\">8475 Michigan Ave. Santa Monica, CA 90645, US</p><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:tahoma, verdana, segoe, sans-serif;line-height:21px;color:#989898;font-size:14px\">Company Number: 07012345</p></td>\n" +
                "                     </tr>\n" +
                "                     <tr style=\"border-collapse:collapse\">\n" +
                "                      <td align=\"center\" style=\"padding:0;Margin:0;padding-top:10px;padding-bottom:20px;font-size:0\">\n" +
                "                       <table class=\"es-table-not-adapt es-social\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                         <tr style=\"border-collapse:collapse\">\n" +
                "                          <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;padding-right:10px\"><img title=\"Twitter\" src=\"https://fbxcbof.stripocdn.email/content/assets/img/social-icons/square-gray/twitter-square-gray.png\" alt=\"Tw\" width=\"24\" height=\"24\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\"></td>\n" +
                "                          <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;padding-right:10px\"><img title=\"Facebook\" src=\"https://fbxcbof.stripocdn.email/content/assets/img/social-icons/square-gray/facebook-square-gray.png\" alt=\"Fb\" width=\"24\" height=\"24\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\"></td>\n" +
                "                          <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;padding-right:10px\"><img title=\"Instagram\" src=\"https://fbxcbof.stripocdn.email/content/assets/img/social-icons/square-gray/instagram-square-gray.png\" alt=\"Ig\" width=\"24\" height=\"24\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\"></td>\n" +
                "                          <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0\"><img title=\"Google+\" src=\"https://fbxcbof.stripocdn.email/content/assets/img/social-icons/square-gray/google-plus-square-gray.png\" alt=\"G+\" width=\"24\" height=\"24\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\"></td>\n" +
                "                         </tr>\n" +
                "                       </table></td>\n" +
                "                     </tr>\n" +
                "                     <tr style=\"border-collapse:collapse\">\n" +
                "                      <td align=\"center\" style=\"padding:0;Margin:0\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:tahoma, verdana, segoe, sans-serif;line-height:18px;color:#989898;font-size:12px\">You are receiving this email because you have visited our site or asked us about regular newsletter. Make sure our messages get to your Inbox (and not your bulk or junk folders). Please add hello@bookkeeping.com to your contacts!</p><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:tahoma, verdana, segoe, sans-serif;line-height:21px;color:#989898;font-size:14px\">Vector graphics designed by <a target=\"_blank\" href=\"http://www.freepik.com/\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;text-decoration:underline;color:#989898;font-size:14px\">Freepik</a>.</p></td>\n" +
                "                     </tr>\n" +
                "                   </table></td>\n" +
                "                 </tr>\n" +
                "               </table></td>\n" +
                "             </tr>\n" +
                "           </table></td>\n" +
                "         </tr>\n" +
                "       </table>\n" +
                "       <table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\">\n" +
                "         <tr style=\"border-collapse:collapse\">\n" +
                "          <td align=\"center\" style=\"padding:0;Margin:0\">\n" +
                "           <table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;width:600px\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">\n" +
                "             <tr style=\"border-collapse:collapse\">\n" +
                "              <td align=\"left\" style=\"Margin:0;padding-left:20px;padding-right:20px;padding-top:30px;padding-bottom:30px\">\n" +
                "               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                 <tr style=\"border-collapse:collapse\">\n" +
                "                  <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:560px\">\n" +
                "                   <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                     <tr style=\"border-collapse:collapse\">\n" +
                "                      <td class=\"es-infoblock made_with\" align=\"center\" style=\"padding:0;Margin:0;line-height:14px;font-size:0;color:#999999\"><a target=\"_blank\" href=\"https://viewstripo.email/?utm_source=templates&utm_medium=email&utm_campaign=finance&utm_content=trigger_newsletter\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;text-decoration:underline;color:#999999;font-size:12px\"><img src=\"https://fbxcbof.stripocdn.email/content/guids/CABINET_9df86e5b6c53dd0319931e2447ed854b/images/64951510234941531.png\" alt width=\"125\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\"></a></td>\n" +
                "                     </tr>\n" +
                "                   </table></td>\n" +
                "                 </tr>\n" +
                "               </table></td>\n" +
                "             </tr>\n" +
                "           </table></td>\n" +
                "         </tr>\n" +
                "       </table></td>\n" +
                "     </tr>\n" +
                "   </table>\n" +
                "  </div>\n" +
                " </body>\n" +
                "</html>";

        StringBuilder mailContent = new StringBuilder();
        mailContent.append(firstPart);
        mailContent.append(secondPart);
        mailContent.append(thirdPart);


        return mailContent.toString();
    }

    @Override
    public Message getMessageById(Long messageId) {
        logger.info("Get Message By ID");
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("The Employee with the ID: " + messageId + " was not found."));
        return message;
    }

    @Override
    public List<Message> getMessagesByMicroentrepreneurshipId(Long microentrepreneurshipId) {
        logger.info("Get Messages By Microentrepreneurship");

        // Verifica si el microemprendimiento existe
        microentrepreneurshipRepository.findById(microentrepreneurshipId)
                .orElseThrow(() -> new EntityNotFoundException("Microemprendimiento no encontrado con ID: " + microentrepreneurshipId));

        // Retorna los mensajes asociados al microemprendimiento
        return messageRepository.findByMicroentrepreneurshipId(microentrepreneurshipId);
    }

    @Override
    public Message changeManagementStatus(Long messageId) {
        logger.info("Change Management Status");

        Message changeStatus = messageRepository.findById(messageId).orElseThrow(() ->
                new ResourceNotFoundException("The Message with ID: " + messageId + "was not found"));

        if(changeStatus.getManagement() == Management.MANAGED) {
            changeStatus.setManagement(Management.UNMANAGED);
            messageRepository.save(changeStatus);
        }else{
            changeStatus.setManagement(Management.MANAGED);
            messageRepository.save(changeStatus);
        }

        return changeStatus;
    }

    @Override
    public List<Message> getAllMessages() {
        logger.info("Get All Messages");
        List<Message> messages = messageRepository.findAll();
        if (messages.isEmpty()) {
            throw new EntityNotFoundException("No messages found");
        }
        return messages;
    }

}
