<div class="modal fade" id="calendarioDialog" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="exampleModalLabel">Asignar calendario</h4>
      </div>
      <g:form  name="asignacionForm" action="asignarCalendario" id="${ptuInstance.id}">

      <div class="modal-body">
        	
          
          <div class="form-group">
            
            <g:select id="calendarioField" name='calendarioDet' 
                class="form-control"
                noSelection="${['null':'Seleccione un calendario']}"
                from="${calendarios}"
                optionKey="id"
                optionValue="${ {it.calendario.comentario +' '+it.calendario.tipo+' '+it.folio+' '+it.calendario.ejercicio} }">
              </g:select>
          </div>
        
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
        
        <g:submitButton class="btn btn-primary" name="aceptar" value="Asignar" />
      </div>
	</g:form>
				

    </div>
  </div>
</div>

<script type="text/javascript">
    $(document).ready(function(){
      $('#asignacionForm').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget); 
        //var pedido = button.data('pedido'); 
        var modal = $(this);
        //modal.find('.modal-title').text('Asignacion manual de pedido: ' + pedido);
        //var surtido=button.data('surtido');
        //modal.find('#surtidoField').val(surtido);
       
      });
      
      $('body').on('shown.bs.modal', '.modal', function () {
          $('[id$=recipient-name]').focus();
      });

      $("form[name='asignacionForm']").submit(function(e){
          console.log('Seleccionando partidas');
          var form=this;
          $('input:checked').each(function(i){
              var ptuDet=$(this).data('ptu');
              console.log('Anexando ptuDet: '+ptuDet);
              $('<input />').attr('type', 'hidden')
                .attr('name', "partidas")
              .attr('value', ptuDet)
              .appendTo(form);
           });
          //e.preventDefault(); //STOP default action
      });

    });
  </script>